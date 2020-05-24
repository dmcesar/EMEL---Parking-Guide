package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingPlaces

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener


class ParkingPlaceInformationLogic {

    private var listener: OnDataReceivedListener? = null

    fun getInfo(latitude: Double, longitude: Double) {

        //TODO: Extra: Get data from API
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}