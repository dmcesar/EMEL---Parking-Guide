package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot

interface OnDataReceivedWithOriginListener {

    fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean)
}