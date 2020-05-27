package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

import com.google.android.gms.maps.model.MarkerOptions
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot

interface OnMapMarkersReceivedListener {

    fun onMapMarkersReceived(markers: HashMap<ParkingLot, MarkerOptions>)
}