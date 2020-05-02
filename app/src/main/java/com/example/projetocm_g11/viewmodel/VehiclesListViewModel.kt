package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.VehiclesLogic

class VehiclesListViewModel : ViewModel(), OnDataReceived {

    private val logic = VehiclesLogic()

    private var listener: OnDataReceived? = null

    /* Observable object */
    var vehicles = ArrayList<Vehicle>()

    fun getAll() {

        this.logic.read()
    }

    fun registerVehicle(vehicle: Vehicle) {

        logic.create(vehicle)
    }

    fun updateVehicle(vehicle: Vehicle) {

        logic.update(vehicle)
    }

    fun deleteVehicle(uuid: String) {

        logic.delete(uuid)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
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