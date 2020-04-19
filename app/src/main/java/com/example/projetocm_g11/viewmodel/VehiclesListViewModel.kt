package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.VehiclesLogic

class VehiclesListViewModel : ViewModel(), OnDataReceived {

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

    fun registerVehicle(vehicle: Vehicle) {

        logic.create(vehicle)
    }

    fun updateVehicle(vehicle: Vehicle) {

        logic.update(vehicle)
    }

    fun deleteVehicle(uuid: String) {

        logic.delete(uuid)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        this.listener?.onDataReceived(list as ArrayList<Vehicle>)
    }
}