package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class FusedLocation private constructor(context: Context) : LocationCallback() {

    // Intervalos de tempo em que a localização é verificada, 20 segundos
    private val TIME_BETWEEN_UPDATES = 5 * 1000L

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
        private val TAG = FusedLocation::class.java.simpleName

        private var activityListener: OnLocationChangedListener? = null
        private var fragmentListener: OnLocationChangedListener? = null
        private var mapListener: OnLocationChangedListener? = null

        private var instance: FusedLocation? = null

        fun registerMapListener(listener: OnLocationChangedListener) {

            this.mapListener = listener
        }

        fun unregisterMapListener() {

            this.mapListener = null
        }

        fun registerActivityListener(listener: OnLocationChangedListener) {

            this.activityListener = listener
        }

        fun unregisterActivityListener() {

            this.activityListener = null
        }

        fun registerFragmentListener(listener: OnLocationChangedListener) {

            this.fragmentListener = listener
        }

        fun unregisterFragmentListener() {

            this.fragmentListener = null
        }

        fun notifyListeners(locationResult: LocationResult) {

            this.activityListener?.onLocationChanged(locationResult)
            this.mapListener?.onLocationChanged(locationResult)
            this.fragmentListener?.onLocationChanged(locationResult)
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

        locationResult?.let { notifyListeners(it) }

        super.onLocationResult(locationResult)
    }
}