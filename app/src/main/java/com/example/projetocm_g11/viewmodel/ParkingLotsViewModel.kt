package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.ParkingLotsLogic

class ParkingLotsViewModel : ViewModel(), OnDataReceived {

    private val logic = ParkingLotsLogic()

    private var listener: OnDataReceived? = null

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        this.listener?.onDataReceived(list as ArrayList<ParkingLot>)
    }
}