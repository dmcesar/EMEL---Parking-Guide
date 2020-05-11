package com.example.projetocm_g11.domain.parkingLots

import android.util.Log
import com.example.projetocm_g11.data.local.list.Storage
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.entities.Type
import com.example.projetocm_g11.data.repositories.ParkingLotsRepository
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class ParkingLotsLogic(private val repository: ParkingLotsRepository) : OnDataReceived {

    private val TAG = ParkingLotsLogic::class.java.simpleName

    private val storage = Storage.getInstance()

    /* Observer is notified with an ArrayList<ParkingLot> */
    private var listener: OnDataReceived? = null

    private fun applyFilters(list: ArrayList<ParkingLot>) {

        Log.i(TAG, "applyFilters() called")

        CoroutineScope(Dispatchers.IO).launch {

            val filters = storage.getAll()

            withContext(Dispatchers.Default) {

                if (filters.size > 0) {

                    var sequence = list.asSequence()

                    Log.i(TAG, "Before filters applied -> ${sequence.toList().size}")

                    for (f in filters) {

                        when (f.value) {

                            "SURFACE" -> sequence.filter { p -> p.getTypeEnum() == Type.SURFACE }

                            "UNDERGROUND" -> sequence.filter { p -> p.getTypeEnum() == Type.UNDERGROUND }

                            "AVAILABLE" -> sequence.filter { p -> p.active == 1 }

                            "UNAVAILABLE" -> sequence.filter { p -> p.active == 0 }

                            "FAVORITE" -> sequence = sequence.filter { p -> p.isFavourite }

                            "ALPHABETICAL" -> sequence = sequence.sortedBy { p -> p.name }

                            else -> sequence = sequence.filter { p -> p.name.contains(f.value) }
                        }
                    }

                    Log.i(TAG, "After filters applied -> ${sequence.toList().size}")

                    withContext(Dispatchers.Main) {

                        notifyDataChanged(ArrayList(sequence.toList()))
                    }

                } else {

                    withContext(Dispatchers.Main) {

                        notifyDataChanged(list)
                    }
                }
            }
        }
    }

    /*
    * Reads data from API or local DB
    * After reading, onDataReceived is called which launches a new Coroutine where the filters are
    * read and applied.
    * Finally, after applying filters, observer is notified
    */
    fun getParkingLots() {

        Log.i(TAG, "getParkingLots()")

        repository.getAll()
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        repository.update(parkingLot)
    }

    /* Notifies ViewModel of data changed */
    private fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        listener?.onDataReceived(ArrayList(list))
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.repository.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.repository.unregisterListener()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "onDataReceived()")

        data?.let {

            it as ArrayList<ParkingLot>

            applyFilters(it)
        }
    }
}