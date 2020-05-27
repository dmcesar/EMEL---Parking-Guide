package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.RetrofitBuilder
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingLots.ParkingLotsLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

const val ENDPOINT = "https://emel.city-platform.com/opendata/"

class ParkingLotsViewModel(application: Application) : AndroidViewModel(application),
    OnDataReceivedListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    private val logic =
        ParkingLotsLogic(
            ParkingLotsRepository(
                localDatabase,
                RetrofitBuilder.getInstance(
                    ENDPOINT
                )
            )
        )

    private var listener: OnDataReceivedListener? = null

    /* Fetches data stored locally */
    fun getAll() {

        this.logic.requestData()
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        this.logic.toggleFavorite(parkingLot)
    }

    fun removeFilters() {

        this.logic.removeFilters()
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