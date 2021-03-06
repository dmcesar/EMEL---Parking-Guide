package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.vehicles

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.VehiclesDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class VehiclesLogic(private val storage: VehiclesDAO) {

    /* Observer is notified with an ArrayList<Vehicle> */
    private var listener: OnDataReceivedListener? = null

    /* Inserts a new vehicle in the local database and calls read() */
    fun create(vehicle: Vehicle) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.insert(vehicle)

            read()
        }
    }

    /* Reads table vehicles from local database and notifies observer */
    fun read() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getAll()

            notifyDataChanged(ArrayList(list))
        }
    }

    /* Updates the entry in table vehicles in the local database*/
    fun update(vehicle: Vehicle) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.update(vehicle)

            read()
        }
    }

    /* Deletes the entry from table vehicles in the local database */
    fun delete(id: String) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.delete(id)

            read()
        }
    }

    /* Notifies observer with UI Thread context */
    private suspend fun notifyDataChanged(list: ArrayList<Vehicle>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }

    /* Registers observer as listener */
    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    /* Deletes observer's reference from listener */
    fun unregisterListener() {

        this.listener = null
    }
}