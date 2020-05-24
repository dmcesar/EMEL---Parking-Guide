package pt.ulusofona.ecati.deisi.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.domain.parkingLots.ParkingLotsLogic

const val ENDPOINT = "https://emel.city-platform.com/opendata/"

class ParkingLotsViewModel(application: Application) : AndroidViewModel(application),
    OnDataReceivedListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    private val logic =
        ParkingLotsLogic(
            localDatabase
        )

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