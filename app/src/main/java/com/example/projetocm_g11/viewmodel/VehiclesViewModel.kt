package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.VehiclesLogic

class VehiclesViewModel : ViewModel(), OnDataReceived {

    private val logic = VehiclesLogic()

    private var listener: OnDataReceived? = null

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)

        this.logic.read()
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        this.listener?.onDataReceived(list as ArrayList<Vehicle>)
    }
}