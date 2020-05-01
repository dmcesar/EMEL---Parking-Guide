package com.example.projetocm_g11.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.Logic
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnDataReceived

class VehiclesListViewModel : ViewModel(), OnDataReceived {

    private val logic = Logic.getInstance()

    private var listener: OnDataReceived? = null

    /* Observable object */
    var vehicles = ArrayList<Vehicle>()

    fun getVehicles() {

        this.logic.getVehicles()
    }

    fun registerVehicle(vehicle: Vehicle) {

        logic.addVehicle(vehicle)
    }

    fun updateVehicle(vehicle: Vehicle) {

        logic.updateVehicle(vehicle)
    }

    fun deleteVehicle(uuid: String) {

        logic.removeVehicle(uuid)
    }

    fun registerListeners(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerVehiclesListener(this)
    }

    fun unregisterListeners() {

        this.listener = null
        this.logic.unregisterVehiclesListener()
    }

    private fun onDataChanged() {

        this.listener?.onDataReceived(this.vehicles)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.vehicles = it as ArrayList<Vehicle> }

        onDataChanged()
    }
}