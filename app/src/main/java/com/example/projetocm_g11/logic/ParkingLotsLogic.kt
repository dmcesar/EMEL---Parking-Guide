package com.example.projetocm_g11.logic

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

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    fun getList() {

        CoroutineScope(Dispatchers.Default).launch {

            val list = ArrayList<ParkingLot>()

            val p1 = ParkingLot(
                "P001",
                "El Corte Inglés",
                true,
                1,
                800,
                700,
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

            list.add(p1)
            list.add(p2)
            list.add(p3)
            list.add(p4)

            withContext(Dispatchers.Main) {

                listener?.onDataReceived(list)
            }
        }
    }
}