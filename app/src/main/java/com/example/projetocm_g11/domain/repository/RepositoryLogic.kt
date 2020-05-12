package com.example.projetocm_g11.domain.repository

import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.repositories.ParkingLotsRepository
import com.example.projetocm_g11.ui.listeners.OnDataReceivedWithOrigin

class RepositoryLogic(private val repository: ParkingLotsRepository) : OnDataReceivedWithOrigin {

    private var listener: OnDataReceivedWithOrigin? = null

    fun getData() {

        this.repository.getAll()
    }

    fun registerListener(listener: OnDataReceivedWithOrigin) {

        this.listener = listener
        this.repository.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.repository.unregisterListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        this.listener?.onDataReceivedWithOrigin(data, updated)
    }
}