package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.repository

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.Connectivity
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.OnConnectivityStatusListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener

abstract class RepositoryLogic(private val repository: ParkingLotsRepository) :
    OnDataReceivedWithOriginListener, OnConnectivityStatusListener {

    private var notified: Boolean? = null
    private var requestedData = false

    fun registerListener() {

        this.repository.registerListener(this)
        Connectivity.registerListener(this)
    }

    open fun unregisterListener() {

        this.repository.unregisterListener()
        Connectivity.unregisterListener()
    }

    private fun getFromLocal() {

        CoroutineScope(Dispatchers.IO).launch {

            repository.getFromLocal()
        }
    }

    private fun getFromRemote() {

        CoroutineScope(Dispatchers.IO).launch {

            repository.getFromRemote()
        }
    }

    fun requestData() {

        CoroutineScope(Dispatchers.IO).launch {

            notified?.let { connected ->

                if (connected) {

                    requestedData = false

                    getFromRemote()
                }

                else {

                    requestedData = false

                    getFromLocal()
                }

            } ?: kotlin.run {

                requestedData = true
            }
        }
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        CoroutineScope(Dispatchers.IO).launch {

            repository.toggleFavorite(parkingLot.id, !parkingLot.isFavourite)

            getFromLocal()
        }
    }

    override fun onConnectivityStatus(connected: Boolean) {

        if(requestedData) {

            if (connected) {

                getFromRemote()
            }

            else {

                getFromLocal()
            }
        }

        else {

            notified = true
        }
    }
}