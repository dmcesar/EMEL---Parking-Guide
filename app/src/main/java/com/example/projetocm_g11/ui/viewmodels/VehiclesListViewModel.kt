package com.example.projetocm_g11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projetocm_g11.data.local.entities.Vehicle
import com.example.projetocm_g11.data.local.room.LocalDatabase
import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener
import com.example.projetocm_g11.domain.vehicles.VehiclesLogic

class VehiclesListViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).vehiclesDAO()

    /* Creates domain component */
    private val logic = VehiclesLogic(localDatabase)

    /* Listener is notified with ArrayList<Vehicle> */
    private var listener: OnDataReceivedListener? = null

    /* Fragment lifecycle variable */
    var vehicles = ArrayList<Vehicle>()

    fun read() {

        this.logic.read()
    }

    fun insert(vehicle: Vehicle) {

        logic.create(vehicle)
    }

    fun update(vehicle: Vehicle) {

        logic.update(vehicle)
    }

    fun delete(id: String) {

        logic.delete(id)
    }

    fun registerListener(listener: OnDataReceivedListener) {

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