package com.example.projetocm_g11.logic

import android.util.Log
import com.example.projetocm_g11.domain.Storage
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehiclesLogic {

    private val TAG = VehiclesLogic::class.java.simpleName

    private var listener: OnDataReceived? = null

    private val storage = Storage.getInstance()

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    fun create(vehicle: Vehicle) {

        Log.i(TAG, "Method create() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.addVehicle(vehicle)

            read()
        }
    }

    fun read() {

        Log.i(TAG, "Method read() called")

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getVehicles()

            withContext(Dispatchers.Main) {

                listener?.onDataReceived(ArrayList(list))
            }
        }
    }

    fun update(vehicle: Vehicle) {

        Log.i(TAG, "Method update() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.updateVehicle(vehicle)

            read()
        }
    }

    fun delete(uuid: String) {

        Log.i(TAG, "Method delete() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.removeVehicle(uuid)

            read()
        }
    }
}