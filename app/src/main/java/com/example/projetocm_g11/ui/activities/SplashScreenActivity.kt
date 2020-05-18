package com.example.projetocm_g11.ui.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import com.example.projetocm_g11.ui.listeners.OnDataReceivedWithOrigin
import com.example.projetocm_g11.ui.viewmodels.ParkingLotsViewModel
import com.example.projetocm_g11.ui.viewmodels.SplashScreenViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*

const val EXTRA_PARKING_LOTS = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.PARKING_LOTS"
const val EXTRA_UPDATED = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.UPDATED"

const val PREFERENCE_SWITCH_THEMES_NOTIFY = "com.example.projetocm_g11.ui.activities.SplashScreenActivity.NOTIFY"

class SplashScreenActivity : AppCompatActivity(), OnDataReceivedWithOrigin {

    private val TAG = SplashScreenActivity::class.java.simpleName

    private lateinit var viewModel: SplashScreenViewModel

    private val splashTimeOut: Long = 3000

    private var minimumTimeOver: Boolean = false

    private var data: ArrayList<ParkingLot>? = null
    private var updated: Boolean? = null

    private fun initSessionPreferences() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        sharedPreferences?.edit()?.putBoolean(PREFERENCE_SWITCH_THEMES_NOTIFY, true)?.apply()
    }

    private fun launchActivity(data: ArrayList<ParkingLot>, updated: Boolean) {

        val intent = Intent(this, MainActivity::class.java)

        intent.putParcelableArrayListExtra(EXTRA_PARKING_LOTS, data)
        intent.putExtra(EXTRA_UPDATED, updated)

        startActivity(intent)
        finish()
    }

    private fun checkInternetConnection(): Boolean {

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnected == true
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)

        initSessionPreferences()

        Handler().postDelayed({

            if(this.updated != null && this.data != null) {

                Log.i(TAG, "Minimum time over with data.")

                launchActivity(this.data!!, this.updated!!)

            } else {

                Log.i(TAG, "Minimum time over without data.")

                minimumTimeOver = true
            }

        }, splashTimeOut)

    }

    override fun onStart() {

        this.viewModel.registerListener(this)

        if(checkInternetConnection()) {

            Log.i(TAG, "Internet connection detected, requesting data from remote.")

            this.viewModel.requestDataFromRemote()

        } else {

            Log.i(TAG, "No internet connection detected, requesting data from local.")

            this.viewModel.requestDataFromLocal()
        }

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        super.onStop()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        if(minimumTimeOver) {

            Log.i(TAG, "Data received after minimum time.")

            launchActivity(data, updated)

        } else {

            Log.i(TAG, "Data received before minimum time.")

            this.data = data
            this.updated = updated
        }
    }
}
