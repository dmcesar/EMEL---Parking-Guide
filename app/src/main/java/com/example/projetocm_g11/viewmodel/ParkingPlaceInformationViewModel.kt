package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.ParkingPlaceInformationLogic

class ParkingPlaceInformationViewModel : ViewModel(), OnDataReceived {

    private var listener: OnDataReceived? = null

    private val logic = ParkingPlaceInformationLogic()

    fun getInfo(latitude: Double, longitude: Double) {

        this.logic.getInfo(latitude, longitude)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.listener?.onDataReceived(data) }
    }
}