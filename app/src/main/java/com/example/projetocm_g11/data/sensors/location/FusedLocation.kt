package com.example.projetocm_g11.data.sensors.location

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class FusedLocation private constructor(context: Context) : LocationCallback() {

    private val TAG = FusedLocation::class.java.simpleName

    // Intervalos de tempo em que a localização é verificada, 20 segundos
    private val TIME_BETWEEN_UPDATES = 20 * 1000L

    // Este atributo é utilizado para configurar os pedidos de localização
    private var locationRequest: LocationRequest? = null

    // Este atributo será utilizado para acedermos à API da FusedLocation
    private var client = FusedLocationProviderClient(context)

    init {
        // Configurar a precisão e os tempos entre atualizações da localização
        locationRequest = LocationRequest()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = TIME_BETWEEN_UPDATES

        // Instanciar o objeto que permite definir as configurações
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
            .build()

        // Aplicar as configurações ao serviço da localização
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)
    }

    companion object {

        private var listener: OnLocationChangedListener? = null
        private var instance: FusedLocation? = null

        fun registerListener(listener: OnLocationChangedListener) {

            this.listener = listener
        }

        fun unregisterListener() {

            this.listener = null
        }

        fun notifyListeners(locationResult: LocationResult) {

            listener?.onLocationChangedListener(locationResult)
        }

        fun start(context: Context) {
            instance = if(instance == null) { FusedLocation(context) } else { instance }
            instance?.startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        client.requestLocationUpdates(locationRequest, this, Looper.myLooper())
    }

    override fun onLocationResult(locationResult: LocationResult?) {

        Log.i(TAG, locationResult?.lastLocation.toString())

        locationResult?.let { notifyListeners(it) }

        super.onLocationResult(locationResult)
    }
}