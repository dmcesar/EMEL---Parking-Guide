package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.contacts

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.VehiclesDAO
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class ContactsLogic(private val storage: VehiclesDAO) {

    private var listener: OnDataReceivedListener? = null

    /* Registers observer as listener */
    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
    }

    /* Deletes observer's reference from listener */
    fun unregisterListener() {

        this.listener = null
    }

    /* Reads table vehicles from local database and notifies observer */
    fun read() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getAll()

            withContext(Dispatchers.Default) {

                notifyDataChanged(ArrayList(list.asSequence().map { v -> v.plate }.toList()))
            }
        }
    }

    /* Notifies observer with UI Thread context */
    private suspend fun notifyDataChanged(list: ArrayList<String>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }
}