package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.MarkerOptions
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.map.MapLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnMapMarkersReceivedListener

class MapViewModel(application: Application) : AndroidViewModel(application), OnMapMarkersReceivedListener {

    private var listener: OnMapMarkersReceivedListener? = null

    private val logic = MapLogic(application)

    fun registerListener(listener: OnMapMarkersReceivedListener) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.logic.unregisterListener()
        this.listener = null
    }

    fun getParkingLotPins(parkingLots: ArrayList<ParkingLot>) {

        this.logic.getParkingLotPins(parkingLots)
    }

    override fun onMapMarkersReceived(markers: HashMap<ParkingLot, MarkerOptions>) {
        this.listener?.onMapMarkersReceived(markers)
    }
}