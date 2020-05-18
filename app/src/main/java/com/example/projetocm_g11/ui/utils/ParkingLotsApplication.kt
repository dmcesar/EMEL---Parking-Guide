package com.example.projetocm_g11.ui.utils

import android.app.Application
import com.example.projetocm_g11.data.sensors.location.FusedLocation

class ParkingLotsApplication : Application() {

    override fun onCreate() {

        super.onCreate()
        FusedLocation.start(this)
    }
}