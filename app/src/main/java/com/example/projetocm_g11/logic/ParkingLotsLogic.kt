package com.example.projetocm_g11.logic

import android.util.Log
import com.example.projetocm_g11.domain.Storage
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.FilterType
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Type
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ParkingLotsLogic {

    private val TAG = ParkingLotsLogic::class.java.simpleName

    private var listener: OnDataReceived? = null

    private val storage = Storage.getInstance()

    private fun applyFilters(list: ArrayList<ParkingLot>, filters: ArrayList<Filter>) {

        Log.i(TAG, "applyFilters() called")

        CoroutineScope(Dispatchers.Default).launch {

            if(filters.size > 0) {

                var sequence = list.asSequence()

                Log.i(TAG, "Before filters applied -> ${sequence.toList().size}")

                for(f in filters) {

                    when (f.type) {

                        FilterType.NAME -> {

                            sequence = sequence.filter { p -> p.name.contains(f.value) }
                        }

                        FilterType.TYPE -> {

                            sequence = if(f.value == "SURFACE") {

                                sequence.filter { p -> p.type == Type.SURFACE }

                            } else sequence.filter { p -> p.type == Type.UNDERGROUND }
                        }

                        FilterType.AVAILABILITY -> {

                            sequence = if(f.value == "AVAILABLE") {

                                sequence.filter { p -> p.active}

                            } else sequence.filter {p -> !p.active}
                        }

                        FilterType.FAVORITE -> {

                            sequence = sequence.filter { p -> p.isFavourite }
                        }

                        FilterType.ALPHABETICAL -> {

                            sequence = sequence.sortedBy { p -> p.name}
                        }
                    }
                }

                Log.i(TAG, "After filters applied -> ${sequence.toList().size}")

                notifyDataChanged(ArrayList(sequence.toList()))
            }

            else {

                notifyDataChanged(list)
            }
        }
    }

    fun getParkingLots() {

        CoroutineScope(Dispatchers.IO).launch {

            val parkingLots = storage.getParkingLots()

            val filters = storage.getFilters()

            /* Applies filters and notifies observer */
            applyFilters(parkingLots, filters)
        }
    }

    fun toggleFavorite(id: String) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.toggleFavorite(id)

            getParkingLots()
        }
    }

    /* Uses Main Thread: Notifies ViewModel of data changed */
    private suspend fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(ArrayList(list))
        }
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}