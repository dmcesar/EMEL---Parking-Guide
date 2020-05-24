package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.repository

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener

class RepositoryLogic(private val repository: ParkingLotsRepository) :
    OnDataReceivedWithOriginListener {

    private var listener: OnDataReceivedWithOriginListener? = null

    fun getFromRemote() {

        CoroutineScope(Dispatchers.IO).launch {

            repository.getFromRemote()
        }
    }

    fun getFromLocal() {

        CoroutineScope(Dispatchers.IO).launch {

            repository.getFromLocal()
        }
    }

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

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