package com.example.projetocm_g11.domain.parkingPlaces

import com.example.projetocm_g11.ui.listeners.OnDataReceived

class ParkingPlaceInformationLogic {

    private var listener: OnDataReceived? = null

    fun getInfo(latitude: Double, longitude: Double) {

        //TODO: Extra: Get data from API
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}