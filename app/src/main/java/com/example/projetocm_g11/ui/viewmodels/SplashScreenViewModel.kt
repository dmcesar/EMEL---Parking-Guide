package com.example.projetocm_g11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.room.LocalDatabase
import com.example.projetocm_g11.data.remote.RetrofitBuilder
import com.example.projetocm_g11.data.repositories.ParkingLotsRepository
import com.example.projetocm_g11.domain.repository.RepositoryLogic
import com.example.projetocm_g11.ui.listeners.OnDataReceivedWithOrigin

class SplashScreenViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedWithOrigin {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    /* Domain component */
    private val logic = RepositoryLogic(
        ParkingLotsRepository(
            localDatabase,
            RetrofitBuilder.getInstance(ENDPOINT)
        )
    )

    private var listener: OnDataReceivedWithOrigin? = null

    fun requestData() {

        this.logic.getData()
    }

    fun registerListener(listener: OnDataReceivedWithOrigin) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        this.listener?.onDataReceivedWithOrigin(data, updated)
    }
}