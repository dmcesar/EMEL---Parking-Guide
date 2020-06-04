package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.parkingZones

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.services.ParkingZonesService
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.API_TOKEN
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnParkingZoneDetailsListener
import retrofit2.Retrofit

class ParkingZonesLogic(private val retrofit: Retrofit) {

    private var listener: OnParkingZoneDetailsListener? = null

    fun getInfo(latitude: Double, longitude: Double) {

        val service = retrofit.create(ParkingZonesService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getZone(token = API_TOKEN, latitude = latitude, longitude = longitude)

            if(response.isSuccessful) {

                response.body()?.let {

                    withContext(Dispatchers.Main) {

                        listener?.onParkingZoneDetailsReceived(it)
                    }
                }
            }

            else withContext(Dispatchers.Main) { listener?.onNoConnectivity() }
        }
    }

    fun registerListener(listener: OnParkingZoneDetailsListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}