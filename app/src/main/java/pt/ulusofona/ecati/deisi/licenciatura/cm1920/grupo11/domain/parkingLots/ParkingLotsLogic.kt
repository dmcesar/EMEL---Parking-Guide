package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingLots

import android.location.Location
import android.util.Log
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.list.Storage
import kotlinx.coroutines.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.Connectivity
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.OnConnectivityStatusListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions
import kotlin.collections.ArrayList

class ParkingLotsLogic(private val repository: ParkingLotsRepository) : OnDataReceivedWithOriginListener,
    OnConnectivityStatusListener {

    private val TAG = ParkingLotsLogic::class.java.simpleName

    private val storage = Storage.getInstance()

    private var hasConnectivity: Boolean? = null
    private var requestedDataFlag = false

    private var listener: OnDataReceivedWithOriginListener? = null
    private var filtersListener: OnDataReceivedListener? = null

    private var userLocation: Location? = null

    private fun processData(receivedData: ArrayList<ParkingLot>, updated: Boolean) {

        CoroutineScope(Dispatchers.IO).launch {

            /* Get filters */
            val filters = storage.getAll()

            var filteredData = receivedData.asSequence()

            /* Apply filters */
            withContext(Dispatchers.Default) {

                if (filters.size > 0) {

                    Log.i(TAG, "Before filters applied -> ${filteredData.toList().size}")

                    for (f in filters) {

                        filteredData = when (f.value) {

                            "SURFACE" -> filteredData.filter { p -> p.getTypeEnum() == Type.SURFACE }

                            "UNDERGROUND" -> filteredData.filter { p -> p.getTypeEnum() == Type.UNDERGROUND }

                            "AVAILABLE" -> filteredData.filter { p -> p.active == 1 }

                            "UNAVAILABLE" -> filteredData.filter { p -> p.active == 0 }

                            "FAVORITE" -> filteredData.filter { p -> p.isFavourite }

                            else -> filteredData.filter { p -> p.name.contains(f.value) }
                        }
                    }

                    Log.i(TAG, "After filters applied -> ${filteredData.toList().size}")
                }
            }

            val processedData = ArrayList(filteredData.toList())

            /* Update distance to user */
            withContext(Dispatchers.Default) {

                userLocation?.let {

                    processedData.forEach { p ->

                        /* Calculate distance */
                        p.distanceToUser = Extensions.calculateDistanceBetween(
                            it,
                            Extensions.toLocation(p.latitude.toDouble(), p.longitude.toDouble())
                        )
                    }
                }
            }

            /* Send data to observers */
            withContext(Dispatchers.Main) {

                listener?.onDataReceivedWithOrigin(processedData, updated)
                filtersListener?.onDataReceived(filters)
            }
        }
    }

    private suspend fun getData(connected: Boolean) {

        requestedDataFlag = false

        if (connected) {

            repository.getFromRemote()

        } else repository.getFromLocal()
    }

    fun requestData(location: Location?) {

        CoroutineScope(Dispatchers.IO).launch {

            userLocation = location

            hasConnectivity?.let { connected ->

                Log.i(TAG, "has connectivity status")

                getData(connected)

            } ?: kotlin.run {

                Log.i(TAG, "no connectivity status")

                requestedDataFlag = true
            }
        }
    }

    fun toggleFavorite(parkingLot: ParkingLot) {

        CoroutineScope(Dispatchers.IO).launch {

            repository.toggleFavorite(parkingLot.id, !parkingLot.isFavourite)

            repository.getFromLocal()
        }
    }

    fun removeFilters() {

        CoroutineScope(Dispatchers.IO).launch {

            userLocation?.let {

                storage.clear()

                requestData(it)
            }
        }
    }

    fun removeFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            userLocation?.let {

                storage.delete(filter)

                requestData(it)
            }
        }
    }

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener
        this.filtersListener = listener as OnDataReceivedListener
        this.repository.registerListener(this)
        Connectivity.registerFragmentListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.filtersListener = null
        this.repository.unregisterListener()
        Connectivity.unregisterFragmentListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        processData(data, updated)
    }

    override fun onConnectivityStatus(connected: Boolean) {

        Log.i(TAG, "connectivity status received")

        hasConnectivity = connected

        if(requestedDataFlag) {

            Log.i(TAG, "requesting data on connectivity status received")

            CoroutineScope(Dispatchers.IO).launch {

                getData(connected)
            }
        }
    }
}