package com.example.projetocm_g11.data.sensors.location

import com.google.android.gms.location.LocationResult

interface OnLocationChangedListener {

    fun onLocationChangedListener(locationResult: LocationResult)
}