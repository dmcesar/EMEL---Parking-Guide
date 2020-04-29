package com.example.projetocm_g11.logic

import com.example.projetocm_g11.domain.data.Filter
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

    private var listener: OnDataReceived? = null

    private lateinit var parkingLots: ArrayList<ParkingLot>

    fun applyFilters(filters: ArrayList<Filter>) {

        CoroutineScope(Dispatchers.Default).launch {

            if(filters.size > 0) {

                val filteredList = ArrayList<ParkingLot>()


                filters.forEach { f -> parkingLots.asSequence().filter { p ->

                    p.type == f.type

                } }

                // TODO: Apply filters

            }

            else {

                notifyDataChanged(parkingLots)
            }
        }
    }

    fun getList() {

        CoroutineScope(Dispatchers.Default).launch {

            parkingLots = ArrayList()

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
    private suspend fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }
}