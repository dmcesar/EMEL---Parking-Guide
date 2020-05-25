package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.main

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.ParkingLotsDAO
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions

class MainLogic(private val local: ParkingLotsDAO) {

    private var listener: OnDataReceivedListener? = null

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    fun getClosestParkingLotCoordinates(userLocation: Location) {

        CoroutineScope(Dispatchers.IO).launch {

            val list = local.getAll()

            val listOrderedByProximity = list.asSequence().sortedBy { p ->
                Extensions.calculateDistanceBetween(
                    Extensions.toLocation(
                        p.latitude.toDouble(),
                        p.longitude.toDouble()
                    ), userLocation
                )
            }.toList()

            val closestParkingLot = listOrderedByProximity[0]

            val coordinatesArrayList = arrayListOf<String>(closestParkingLot.latitude, closestParkingLot.longitude)

            withContext(Dispatchers.Main) {

                listener?.onDataReceived(coordinatesArrayList)
            }
        }
    }
}