package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories

import android.util.Log
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.ParkingLotsDAO
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.services.ParkingLotsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import retrofit2.Retrofit

private const val API_TOKEN = "93600bb4e7fee17750ae478c22182dda"

class ParkingLotsRepository(private val local: ParkingLotsDAO, private val remote: Retrofit) {

    private val TAG = ParkingLotsRepository::class.java.simpleName

    /* Notifies observer (ParkingLotsLogic) with ArrayList<ParkingLots> */
    private var listener: OnDataReceivedWithOriginListener? = null

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

    suspend fun toggleFavorite(id: String, status: Boolean) {

        local.updateFavoriteStatus(id, status)
    }

    suspend fun getFromRemote() {

        Log.i(TAG, "getFromRemote()")

        val list = ArrayList<ParkingLot>()

        /* Create endpoint service */
        val service = remote.create(ParkingLotsService::class.java)

        /* Send request with authentication token receive response */
        val response = service.getAll(token = API_TOKEN)

        if(response.isSuccessful) {

            response.body()?.let {

                /* Creates ParkingLot object for each response list item */
                it.forEach { obj ->

                    val parkingLot =
                        ParkingLot(
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

    private fun notifyObserver(list: ArrayList<ParkingLot>, updated: Boolean) {

        listener?.onDataReceivedWithOrigin(list, updated)
    }

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}