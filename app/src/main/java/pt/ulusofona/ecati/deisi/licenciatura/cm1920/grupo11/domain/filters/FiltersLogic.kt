package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.filters

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.list.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class FiltersLogic {

    private val storage = Storage.getInstance()

    /* Observer is notified with an ArrayList<Filter> */
    private var listener: OnDataReceivedListener? = null

    /* Reads list of filters from shared preferences and notifies observer */
    fun read() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getAll()

            notifyDataChanged(list)
        }
    }

    /* Inserts filter in set kept in shared preferences */
    fun insert(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getAll()

            if(!list.contains(filter)) {

                when (filter.value) {

                    "SURFACE" -> {

                        val opposite =
                            Filter(
                                "UNDERGROUND"
                            )

                        if (list.contains(opposite)) {

                            storage.delete(opposite)
                        }
                    }

                    "UNDERGROUND" -> {

                        val opposite =
                            Filter(
                                "SURFACE"
                            )

                        if (list.contains(opposite)) {

                            storage.delete(opposite)
                        }
                    }
                }

                storage.insert(filter)

                read()
            }
        }
    }

    /* Removes filter from set kept in shared preferences */
    fun delete(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.delete(filter)

            read()
        }
    }

    fun deleteAll() {

        CoroutineScope(Dispatchers.IO).launch {

            storage.clear()

            read()
        }
    }

    /* Notifies observer with UI Thread context */
    private suspend fun notifyDataChanged(list: ArrayList<Filter>) {

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