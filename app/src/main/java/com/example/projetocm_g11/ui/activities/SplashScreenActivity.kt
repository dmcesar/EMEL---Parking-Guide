package com.example.projetocm_g11.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import com.example.projetocm_g11.ui.viewmodels.ParkingLotsViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*

const val EXTRA_PARKING_LOTS = "com.example.projetocm_g11.ui.activities.PARKING_LOTS_LIST"

class SplashScreenActivity : AppCompatActivity() {


    private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }, splashTimeOut)
    }

}
