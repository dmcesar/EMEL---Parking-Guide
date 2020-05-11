package com.example.projetocm_g11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.list.Storage
import com.example.projetocm_g11.data.local.room.LocalDatabase
import com.example.projetocm_g11.data.remote.RetrofitBuilder
import com.example.projetocm_g11.data.repositories.ParkingLotsRepository
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import com.example.projetocm_g11.domain.parkingLots.ParkingLotsLogic

const val ENDPOINT = "https://emel.city-platform.com/opendata/"

class ParkingLotsViewModel(application: Application) : AndroidViewModel(application), OnDataReceived {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    private val logic = ParkingLotsLogic(
        ParkingLotsRepository(
            localDatabase,
            RetrofitBuilder.getInstance(ENDPOINT)
        )
    )

    private var listener: OnDataReceived? = null

    /* Observable object */
    var parkingLots = ArrayList<ParkingLot>()

    fun getAll() {

        this.logic.getParkingLots()
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        this.logic.toggleFavorite(parkingLot)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    private fun notifyDataChanged() {

        this.listener?.onDataReceived(ArrayList(this.parkingLots))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.parkingLots = it as ArrayList<ParkingLot> }

        notifyDataChanged()
    }
}