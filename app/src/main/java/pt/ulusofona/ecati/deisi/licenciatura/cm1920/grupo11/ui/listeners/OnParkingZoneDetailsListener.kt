package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse

interface OnParkingZoneDetailsListener {

    fun onParkingZoneDetailsReceived(data: ParkingZoneResponse)

    fun onNoConnectivity()
}