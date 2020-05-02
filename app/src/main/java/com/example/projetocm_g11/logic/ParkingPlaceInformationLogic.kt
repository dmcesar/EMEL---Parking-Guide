package com.example.projetocm_g11.logic

import com.example.projetocm_g11.interfaces.OnDataReceived

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