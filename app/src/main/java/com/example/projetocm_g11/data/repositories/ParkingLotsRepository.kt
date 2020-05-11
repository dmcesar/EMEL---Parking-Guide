package com.example.projetocm_g11.data.repositories

import android.util.Log
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.room.dao.ParkingLotsDAO
import com.example.projetocm_g11.data.remote.requests.ParkingLotsRequestHeader
import com.example.projetocm_g11.data.remote.services.ParkingLotsService
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

private const val API_TOKEN = "93600bb4e7fee17750ae478c22182dda"

class ParkingLotsRepository(private val local: ParkingLotsDAO, private val remote: Retrofit) {

    private val TAG = ParkingLotsRepository::class.java.simpleName

    /* Notifies observer (ParkingLotsLogic) with ArrayList<ParkingLots> */
    private var listener: OnDataReceived? = null

    /*
    * Requests all parking lots from API.
    * If response is not successful, read potentially outdated data from local storage.
    */
    fun getAll() {

        Log.i(TAG, "getAll()")

        CoroutineScope(Dispatchers.IO).launch {

            /* Tries to read data from remote API */
            getFromRemote()
        }
    }

    private suspend fun getFromRemote() {

        Log.i(TAG, "getFromRemote()")

        val list = ArrayList<ParkingLot>()

        /* Create endpoint service */
        val service = remote.create(ParkingLotsService::class.java)

        /* Send request with authentication token receive response */
        val response = service.getAll(token = API_TOKEN)

        if(response.isSuccessful) {

            Log.i(TAG, "Response successful!")

            response.body()?.let {

                /* Creates ParkingLot object for each response list item */
                it.forEach { obj ->

                    val parkingLot = ParkingLot(
                        identityID = obj.identityID,
                        id = obj.id,
                        latitude = obj.latitude,
                        longitude = obj.longitude,
                        active = obj.active,
                        lastUpdatedAt = obj.lastUpdatedAt,
                        type = obj.type,
                        maxCapacity = obj.maxCapacity,
                        name = obj.name,
                        occupancy = obj.occupancy
                    )

                    list.add(parkingLot)

                    /* Update local DB with received item */
                    local.insert(parkingLot)
                }
            }

            notifyObserver(list)

        } else {

            Log.i(TAG, "Response unsuccessful!")

            getFromLocal()
        }
    }

    private suspend fun getFromLocal() {

        Log.i(TAG, "getFromLocal()")

        val list = ArrayList(local.getAll())

        notifyObserver(list)
    }

    /*
    * Updates parking lot's "favorite" status.
    * Operation is done locally since API does not provide parameter.
    * After operation is finished, read locally updated data from repository and notify observer
    */
    fun update(parkingLot: ParkingLot) {

        CoroutineScope(Dispatchers.IO).launch {

            local.update(parkingLot)

            getFromLocal()
        }
    }

    private fun notifyObserver(list: ArrayList<ParkingLot>) {

        this.listener?.onDataReceived(list)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}