package com.example.projetocm_g11.domain

import android.util.Log
import com.example.projetocm_g11.OnDataReceived
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListLogic {

    private val TAG = ListLogic::class.java.simpleName

    private var listener: OnDataReceived? = null

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    private suspend fun notifyDataChanged(list: ArrayList<ParkingLot>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }

    fun fetchAll() {

        Log.i(TAG, "Method fetchAll() called")

        CoroutineScope(Dispatchers.IO).launch {

            val list = ArrayList<ParkingLot>()

            val p1 = ParkingLot(Type.SURFACE, "P1", "Lisboa")
            val p2 = ParkingLot(Type.SURFACE, "P2", "Cascais")
            val p3 = ParkingLot(Type.SURFACE, "P3", "Sintra")
            val p4 = ParkingLot(Type.UNDERGROUND, "P4", "Porto")

            list.add(p1)
            list.add(p2)
            list.add(p3)
            list.add(p4)
            notifyDataChanged(list)

            /*
            val list = TODO: API GET request...

            notifyDataChanged(list)
            */
        }
    }
}