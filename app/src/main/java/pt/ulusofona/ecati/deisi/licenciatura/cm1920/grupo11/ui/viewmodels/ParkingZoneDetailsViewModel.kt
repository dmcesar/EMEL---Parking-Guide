package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.RetrofitBuilder
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingZones.ParkingZonesLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnParkingZoneDetailsListener

class ParkingZoneDetailsViewModel : ViewModel(),
    OnParkingZoneDetailsListener {

    private var listener: OnParkingZoneDetailsListener? = null

    private val logic =
        ParkingZonesLogic(RetrofitBuilder.getInstance(ENDPOINT))

    fun getInfo(latitude: Double, longitude: Double) {

        this.logic.getInfo(latitude, longitude)
    }

    fun registerListener(listener: OnParkingZoneDetailsListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    override fun onParkingZoneDetailsReceived(data: ParkingZoneResponse) {
        this.listener?.onParkingZoneDetailsReceived(data)
    }

    override fun onNoConnectivity() {

        this.listener?.onNoConnectivity()
    }
}