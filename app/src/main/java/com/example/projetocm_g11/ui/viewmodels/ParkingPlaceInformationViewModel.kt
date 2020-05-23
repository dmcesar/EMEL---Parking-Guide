package com.example.projetocm_g11.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener
import com.example.projetocm_g11.domain.parkingPlaces.ParkingPlaceInformationLogic

class ParkingPlaceInformationViewModel : ViewModel(), OnDataReceivedListener {

    private var listener: OnDataReceivedListener? = null

    private val logic =
        ParkingPlaceInformationLogic()

    fun getInfo(latitude: Double, longitude: Double) {

        this.logic.getInfo(latitude, longitude)
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.listener?.onDataReceived(data) }
    }
}