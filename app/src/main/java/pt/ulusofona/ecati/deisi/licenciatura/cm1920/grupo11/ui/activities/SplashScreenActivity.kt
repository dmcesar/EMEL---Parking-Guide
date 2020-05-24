package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.SplashScreenViewModel

const val EXTRA_DATA = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.DATA"
const val EXTRA_DATA_FROM_REMOTE = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.DATA_FROM_REMOTE"

const val PREFERENCE_SWITCH_THEMES_NOTIFY = "pt.ulusofona.ecati.SplashScreenActivity.NOTIFY"

class SplashScreenActivity : AppCompatActivity(),
    OnDataReceivedWithOriginListener {

    private val TAG = SplashScreenActivity::class.java.simpleName

    private lateinit var viewModel: SplashScreenViewModel

    private val splashTimeOut: Long = 3000

    private var minimumTimeOver: Boolean = false

    private var data: ArrayList<ParkingLot>? = null
    private var updated: Boolean? = null

    private fun launchActivity(data: ArrayList<ParkingLot>, updated: Boolean) {

        val intent = Intent(this, MainActivity::class.java)

        intent.putParcelableArrayListExtra(EXTRA_DATA, data)
        intent.putExtra(EXTRA_DATA_FROM_REMOTE, updated)

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
