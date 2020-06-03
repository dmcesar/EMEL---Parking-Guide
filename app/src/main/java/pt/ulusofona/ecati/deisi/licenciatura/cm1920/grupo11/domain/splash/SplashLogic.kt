package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.splash

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.Connectivity
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.OnConnectivityStatusListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions

class SplashLogic(private val repository: ParkingLotsRepository) : OnDataReceivedWithOriginListener,
    OnConnectivityStatusListener {

    private var listener: OnDataReceivedWithOriginListener? = null

    private var userLocation: Location? = null
    private var hasConnectivity: Boolean? = null

    /* Flag raised when data is requested (when no connectivity status has been received) */
    private var requestedDataFlag: Boolean = false

    private suspend fun processData(data: ArrayList<ParkingLot>, updated: Boolean) {

        /* For each parking lot, calculate distance to user */
        this.userLocation?.let {

            data.forEach { p ->

                p.distanceToUser = Extensions.calculateDistanceBetween(
                    it,
                    Extensions.toLocation(p.latitude.toDouble(), p.longitude.toDouble())
                )
            }
        }

        withContext(Dispatchers.Main) {

            listener?.onDataReceivedWithOrigin(data, updated)
        }
    }

    /* Requests data from remote if connected, else from local */
    private suspend fun getData(connected: Boolean) {

        requestedDataFlag = false

        if (connected) {

            repository.getFromRemote()

        } else repository.getFromLocal()
    }

    /* Calls getData if connectivity status has been received, else raises flag to getData when status is received */
    fun requestData(location: Location) {

        CoroutineScope(Dispatchers.IO).launch {

            userLocation = location

            hasConnectivity?.let { connected ->

                getData(connected)

            } ?: kotlin.run {

                requestedDataFlag = true
            }
        }
    }

    /* Observer/Observable */
    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener
        this.repository.registerListener(this)
        Connectivity.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.repository.unregisterListener()
        Connectivity.unregisterListener()
    }

    /* Receives data and notifies observer */
    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        CoroutineScope(Dispatchers.Default).launch {

            processData(data, updated)
        }
    }

    /* Received connectivity status and acts upon value */
    override fun onConnectivityStatus(connected: Boolean) {

        if(requestedDataFlag) {

            CoroutineScope(Dispatchers.IO).launch {

                getData(connected)
            }
        }

        else {

            hasConnectivity = connected
        }
    }
}