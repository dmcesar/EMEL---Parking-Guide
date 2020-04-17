package com.example.projetocm_g11.logic

import com.example.projetocm_g11.interfaces.OnDataReceived

class ParkingLotsLogic {

    private var listener: OnDataReceived? = null

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}