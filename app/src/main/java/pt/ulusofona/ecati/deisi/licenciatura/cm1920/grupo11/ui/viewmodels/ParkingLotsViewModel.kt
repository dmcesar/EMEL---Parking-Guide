package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.RetrofitBuilder
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingLots.ParkingLotsLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNotificationEventListener

const val ENDPOINT = "https://emel.city-platform.com/opendata/"

class ParkingLotsViewModel(application: Application) : AndroidViewModel(application),
    OnDataReceivedWithOriginListener, OnDataReceivedListener, OnNotificationEventListener {

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

    private var listener: OnDataReceivedWithOriginListener? = null
    private var filtersListener: OnDataReceivedListener? = null
    private var notificationListener: OnNotificationEventListener? = null

    /* Fetches data stored locally */
    fun getAll(userLocation: Location?) {

        this.logic.requestData(userLocation)
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        this.logic.toggleFavorite(parkingLot)
    }

    fun removeFilters() {

        this.logic.removeFilters()
    }

    fun removeFilter(filter: Filter) {

        this.logic.removeFilter(filter)
    }

    fun checkIfUserIsNearParkingLot(parkingLots: ArrayList<ParkingLot>){

        this.logic.checkIfUserIsNearParkingLot(parkingLots)
    }

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener
        this.filtersListener = listener as OnDataReceivedListener
        this.notificationListener = listener as OnNotificationEventListener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.filtersListener = null
        this.notificationListener = null
        this.logic.unregisterListener()
    }

    private fun notifyDataChanged(list: ArrayList<ParkingLot>, updated: Boolean) {

        this.listener?.onDataReceivedWithOrigin(ArrayList(list), updated)
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        notifyDataChanged(data, updated)
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        this.filtersListener?.onDataReceived(data)
    }

    override fun onNotificationEvent(parkingLot: ParkingLot) {

        this.notificationListener?.onNotificationEvent(parkingLot)
    }
}