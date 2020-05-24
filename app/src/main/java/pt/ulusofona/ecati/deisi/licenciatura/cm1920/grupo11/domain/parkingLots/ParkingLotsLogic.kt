package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingLots

import android.util.Log
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.list.Storage
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.ParkingLotsDAO
import kotlinx.coroutines.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import kotlin.collections.ArrayList

class ParkingLotsLogic(private val database: ParkingLotsDAO) {

    private val TAG = ParkingLotsLogic::class.java.simpleName

    private val storage = Storage.getInstance()

    /* Observer is notified with an ArrayList<ParkingLot> */
    private var listener: OnDataReceivedListener? = null

    private fun applyFilters(unfilteredList: ArrayList<ParkingLot>, filters: ArrayList<Filter>): ArrayList<ParkingLot> {

        if (filters.size > 0) {

            var filteredList = unfilteredList.asSequence()

            Log.i(TAG, "Before filters applied -> ${filteredList.toList().size}")

            for (f in filters) {

                when (f.value) {

                    "SURFACE" -> filteredList = filteredList.filter { p -> p.getTypeEnum() == Type.SURFACE }

                    "UNDERGROUND" -> filteredList = filteredList.filter { p -> p.getTypeEnum() == Type.UNDERGROUND }

                    "AVAILABLE" -> filteredList = filteredList.filter { p -> p.active == 1 }

                    "UNAVAILABLE" -> filteredList = filteredList.filter { p -> p.active == 0 }

                    "FAVORITE" -> filteredList = filteredList.filter { p -> p.isFavourite }

                    "ALPHABETICAL" -> filteredList = filteredList.sortedBy { p -> p.name }

                    else -> filteredList = filteredList.filter { p -> p.name.contains(f.value) }
                }
            }

            Log.i(TAG, "After filters applied -> ${filteredList.toList().size}")

            return ArrayList(filteredList.toList())

        } else {

            return unfilteredList
        }
    }

    /* Read locally stored data and notify observer */
    fun getParkingLots() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = ArrayList(database.getAll())
            val filters = storage.getAll()

            val filteredList = applyFilters(list, filters)

            notifyDataChanged(filteredList)
        }
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        CoroutineScope(Dispatchers.IO).launch {

            database.updateFavoriteStatus(parkingLot.id, !parkingLot.isFavourite)

            getParkingLots()
        }
    }

    /* Notifies ViewModel */
    private suspend fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(ArrayList(list))
        }
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}