package com.example.projetocm_g11.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.sensors.location.FusedLocation
import com.example.projetocm_g11.data.sensors.location.OnLocationChangedListener
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.map_view

const val REQUEST_CODE = 100

class MapFragment : PermissionsFragment(REQUEST_CODE), OnMapReadyCallback,
    OnLocationChangedListener {

    private val TAG = MapFragment::class.java.simpleName

    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.map_view.onCreate(savedInstanceState)

        return view
    }

    override fun onStart() {

        super.onRequestPermissions(activity?.baseContext!!, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))

        super.onStart()
    }

    override fun onRequestPermissionsSuccess() {

        FusedLocation.registerListener(this)
        map_view.getMapAsync(this)
        map_view.onResume()
    }

    override fun onRequestPermissionsFailure() {
        Log.i(TAG, "request failed")
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
    }

    override fun onLocationChangedListener(locationResult: LocationResult) {
        val location = locationResult.lastLocation
    }
}