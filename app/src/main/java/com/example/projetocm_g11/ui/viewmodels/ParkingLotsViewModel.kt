package com.example.projetocm_g11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.room.LocalDatabase
import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener
import com.example.projetocm_g11.domain.parkingLots.ParkingLotsLogic

const val ENDPOINT = "https://emel.city-platform.com/opendata/"

class ParkingLotsViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    private val logic = ParkingLotsLogic(localDatabase)

    private var listener: OnDataReceivedListener? = null

    /* Fetches data stored locally */
    fun getAll() {

        this.logic.getParkingLots()
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        this.logic.toggleFavorite(parkingLot)
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    private fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        this.listener?.onDataReceived(ArrayList(list))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { notifyDataChanged(data as ArrayList<ParkingLot>) }
    }
}