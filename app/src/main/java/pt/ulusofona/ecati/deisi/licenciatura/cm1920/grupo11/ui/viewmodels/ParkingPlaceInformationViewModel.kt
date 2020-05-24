package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingPlaces.ParkingPlaceInformationLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class ParkingPlaceInformationViewModel : ViewModel(),
    OnDataReceivedListener {

    private var listener: OnDataReceivedListener? = null

    private val logic =
        ParkingPlaceInformationLogic()

    fun getInfo(latitude: Double, longitude: Double) {

        this.logic.getInfo(latitude, longitude)
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.listener?.onDataReceived(data) }
    }
}