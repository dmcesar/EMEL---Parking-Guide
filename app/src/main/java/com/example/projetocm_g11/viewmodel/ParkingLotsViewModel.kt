package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.Logic
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnDataReceived

class ParkingLotsViewModel : ViewModel(), OnDataReceived {

    /* Init instance of logic */
    private var logic: Logic = Logic.getInstance()

    private var listener: OnDataReceived? = null

    /* Observable object */
    var parkingLots = ArrayList<ParkingLot>()

    /* Fetches list of parking lots*/
    private fun getParkingLots() {

        this.logic.getParkingLots()
    }

    /* Toggles parking lots as favorite */
    fun toggleFavorite(id: String) {

        this.logic.toggleFavorite(id)
    }

    fun registerListeners(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerParkingLotsListener(this)
    }

    fun unregisterListeners() {

        this.listener = null
        this.logic.unregisterParkingLotsListener()
    }

    /* Notify observer of change in data */
    private fun notifyDataChanged() {

        this.listener?.onDataReceived(this.parkingLots)
    }

    /* Method called by observable (logic) */
    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.parkingLots = it as ArrayList<ParkingLot> }

        notifyDataChanged()
    }
}