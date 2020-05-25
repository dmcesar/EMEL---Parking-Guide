package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.main.MainLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class MainViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    private var listener: OnDataReceivedListener? = null

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    private val logic = MainLogic(localDatabase)

    fun getClosestParkingLotCoordinates(userLocation: Location) {

        this.logic.getClosestParkingLotCoordinates(userLocation)
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        this.listener?.onDataReceived(data)
    }
}