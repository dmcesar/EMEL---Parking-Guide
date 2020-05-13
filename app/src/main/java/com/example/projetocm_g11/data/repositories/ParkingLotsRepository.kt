package com.example.projetocm_g11.data.repositories

import android.util.Log
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.room.dao.ParkingLotsDAO
import com.example.projetocm_g11.data.remote.requests.ParkingLotsRequestHeader
import com.example.projetocm_g11.data.remote.services.ParkingLotsService
import com.example.projetocm_g11.ui.listeners.OnDataReceived
import com.example.projetocm_g11.ui.listeners.OnDataReceivedWithOrigin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

private const val API_TOKEN = "93600bb4e7fee17750ae478c22182dda"

class ParkingLotsRepository(private val local: ParkingLotsDAO, private val remote: Retrofit) {

    private val TAG = ParkingLotsRepository::class.java.simpleName

    /* Notifies observer (ParkingLotsLogic) with ArrayList<ParkingLots> */
    private var listener: OnDataReceivedWithOrigin? = null

    private suspend fun updateLocal(remoteData: ArrayList<ParkingLot>): ArrayList<ParkingLot> {

        val locallyUpdatedList = ArrayList<ParkingLot>()

        remoteData.forEach { p ->

            val cntOccurrences = local.getCountWithID(p.id)

            /* If entry with same ID already exists, update data and add updated data to output list */
            if(cntOccurrences == 1) {

                local.updateData(p.id, p.active, p.lastUpdatedAt, p.occupancy)
                locallyUpdatedList.add(local.get(p.id))

            } else{

                local.insert(p)
                locallyUpdatedList.add(p)
            }
        }

        return locallyUpdatedList
    }

    suspend fun getFromRemote() {

        Log.i(TAG, "getFromRemote()")

        val list = ArrayList<ParkingLot>()

        /* Create endpoint service */
        val service = remote.create(ParkingLotsService::class.java)

        Log.i(TAG, "waiting response")

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
                }
            }

            /* Update local repository based on new data (preserves favorite status) */
            val locallyUpdatedList = updateLocal(list)

            /* Notify observer with latest data */
            notifyObserver(locallyUpdatedList, true)

        } else {

            Log.i(TAG, "Response unsuccessful!")

            getFromLocal()
        }
    }

    suspend fun getFromLocal() {

        Log.i(TAG, "getFromLocal()")

        val list = ArrayList(local.getAll())

        notifyObserver(list, false)
    }

    private suspend fun notifyObserver(list: ArrayList<ParkingLot>, updated: Boolean) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceivedWithOrigin(list, updated)
        }
    }

    fun registerListener(listener: OnDataReceivedWithOrigin) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}