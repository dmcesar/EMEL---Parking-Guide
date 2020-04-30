package com.example.projetocm_g11.logic

import android.util.Log
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

    private val parkingLots = mutableListOf<ParkingLot>()

    fun applyFilters(filters: ArrayList<Filter>) {

        Log.i(TAG, "applyFilters() called")

        CoroutineScope(Dispatchers.Default).launch {

            if(filters.size > 0) {

                var sequence = parkingLots.asSequence()

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

                        FilterType.ALPHABETICAL -> {

                            sequence = sequence.sortedBy { p -> p.name}
                        }
                    }
                }

                Log.i(TAG, "After filters applied -> ${sequence.toList().size}")

                notifyDataChanged(ArrayList(sequence.toList()))
            }

            else {

                notifyDataChanged(parkingLots)
            }
        }
    }

    fun getList() {

        Log.i(TAG, "getList() called")

        CoroutineScope(Dispatchers.Default).launch {

            parkingLots.clear()

            val p1 = ParkingLot(
                "P001",
                "El Corte Inglés",
                true,
                1,
                800,
                750,
                Date(),
                38.75463622,
                -9.3414141,
                Type.UNDERGROUND
            )

            val p2 = ParkingLot(
                "P002",
                "Campo Grande",
                true,
                2,
                200,
                40,
                Date(),
                76.3245424,
                90.324143,
                Type.SURFACE
            )

            val p3 = ParkingLot(
                "P003",
                "Baia de Cascais",
                false,
                3,
                50,
                50,
                Date(),
                -50.324324,
                -7.214122,
                Type.UNDERGROUND
            )

            val p4 = ParkingLot(
                "P019",
                "CascaisShopping",
                true,
                4,
                1000,
                650,
                Date(),
                -80.24324,
                43.541343,
                Type.UNDERGROUND
            )

            parkingLots.add(p1)
            parkingLots.add(p2)
            parkingLots.add(p3)
            parkingLots.add(p4)

            notifyDataChanged(parkingLots)
        }
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    /* Uses Main Thread: Notifies ViewModel of data changed*/
    private suspend fun notifyDataChanged(list: MutableList<ParkingLot>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(ArrayList(list))
        }
    }
}