package pt.ulusofona.ecati.deisi.ui.listeners

import pt.ulusofona.ecati.deisi.data.local.entities.ParkingLot

interface OnDataReceivedWithOriginListener {

    fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean)
}