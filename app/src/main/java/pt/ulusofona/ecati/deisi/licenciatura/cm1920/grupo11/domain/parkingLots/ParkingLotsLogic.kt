package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingLots

import android.util.Log
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.list.Storage
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.ParkingLotsDAO
import kotlinx.coroutines.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.repository.RepositoryLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import kotlin.collections.ArrayList

class ParkingLotsLogic(repository: ParkingLotsRepository) : RepositoryLogic(repository) {

    private val TAG = ParkingLotsLogic::class.java.simpleName

    private val storage = Storage.getInstance()

    private var listener: OnDataReceivedListener? = null

    private fun applyFilters(unfilteredList: ArrayList<ParkingLot>) {

        CoroutineScope(Dispatchers.IO).launch {

            val filters = storage.getAll()

            withContext(Dispatchers.Default) {

                if (filters.size > 0) {

                    var filteredList = unfilteredList.asSequence()

                    Log.i(TAG, "Before filters applied -> ${filteredList.toList().size}")

                    for (f in filters) {

                        filteredList = when (f.value) {

                            "SURFACE" -> filteredList.filter { p -> p.getTypeEnum() == Type.SURFACE }

                            "UNDERGROUND" -> filteredList.filter { p -> p.getTypeEnum() == Type.UNDERGROUND }

                            "AVAILABLE" -> filteredList.filter { p -> p.active == 1 }

                            "UNAVAILABLE" -> filteredList.filter { p -> p.active == 0 }

                            "FAVORITE" -> filteredList.filter { p -> p.isFavourite }

                            else -> filteredList.filter { p -> p.name.contains(f.value) }
                        }
                    }

                    Log.i(TAG, "After filters applied -> ${filteredList.toList().size}")

                    notifyDataChanged(ArrayList(filteredList.toList()))

                } else {

                    notifyDataChanged(unfilteredList)
                }
            }
        }
    }

    fun removeFilters() {

        CoroutineScope(Dispatchers.IO).launch {

            storage.clear()

            super.requestData()
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
        super.registerListener()
    }

    override fun unregisterListener() {

        this.listener = null
        super.unregisterListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        applyFilters(data)
    }
}