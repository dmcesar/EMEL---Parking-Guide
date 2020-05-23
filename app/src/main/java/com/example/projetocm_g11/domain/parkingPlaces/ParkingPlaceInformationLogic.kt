package com.example.projetocm_g11.domain.parkingPlaces

import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener

class ParkingPlaceInformationLogic {

    private var listener: OnDataReceivedListener? = null

    fun getInfo(latitude: Double, longitude: Double) {

        //TODO: Extra: Get data from API
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}