package com.example.projetocm_g11.domain.vehicles

import android.util.Log
import com.example.projetocm_g11.data.local.list.Storage
import com.example.projetocm_g11.data.local.entities.Vehicle
import com.example.projetocm_g11.data.local.room.dao.VehiclesDAO
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehiclesLogic(private val storage: VehiclesDAO) {

    private val TAG = VehiclesLogic::class.java.simpleName

    /* Observer is notified with an ArrayList<Vehicle> */
    private var listener: OnDataReceived? = null

    /* Inserts a new vehicle in the local database and calls read() */
    fun create(vehicle: Vehicle) {

        Log.i(TAG, "create() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.insert(vehicle)

            read()
        }
    }

    /* Reads table vehicles from local database and notifies observer */
    fun read() {

        Log.i(TAG, "read() called")

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getAll()

            notifyDataChanged(ArrayList(list))
        }
    }

    /* Updates the entry in table vehicles in the local database*/
    fun update(vehicle: Vehicle) {

        Log.i(TAG, "update() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.update(vehicle)

            read()
        }
    }

    /* Deletes the entry from table vehicles in the local database */
    fun delete(vehicle: Vehicle) {

        Log.i(TAG, "delete() called")

        CoroutineScope(Dispatchers.IO).launch {

            storage.delete(vehicle)

            read()
        }
    }

    /* Notifies observer with UI Thread context */
    private suspend fun notifyDataChanged(list: ArrayList<Vehicle>) {

        Log.i(TAG, "notifyDataChanged() called")

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }

    /* Registers observer as listener */
    fun registerListener(listener: OnDataReceived) {

        Log.i(TAG, "registerListener() called")

        this.listener = listener
    }

    /* Deletes observer's reference from listener */
    fun unregisterListener() {

        Log.i(TAG, "unregisterListener() called")

        this.listener = null
    }
}