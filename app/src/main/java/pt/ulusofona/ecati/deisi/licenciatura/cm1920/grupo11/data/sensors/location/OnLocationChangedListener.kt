package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location

import com.google.android.gms.location.LocationResult

interface OnLocationChangedListener {

    fun onLocationChangedListener(locationResult: LocationResult)
}