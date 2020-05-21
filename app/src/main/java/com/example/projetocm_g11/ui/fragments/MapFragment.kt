package com.example.projetocm_g11.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.sensors.location.FusedLocation
import com.example.projetocm_g11.data.sensors.location.OnLocationChangedListener
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.map_view

const val REQUEST_CODE = 100

class MapFragment : PermissionsFragment(REQUEST_CODE), OnMapReadyCallback,
    OnLocationChangedListener {

    private val TAG = MapFragment::class.java.simpleName

    private var map: GoogleMap? = null

    private fun setMapMarker() {

        arguments?.let {

            val parkingLot = it.getParcelable<ParkingLot>(EXTRA_PARKING_LOT)

            parkingLot?.let {

                /* Create marker with park coordinates and name */
                val marker = MarkerOptions()
                    .title(parkingLot.name)
                    .position(LatLng(parkingLot.latitude.toDouble(), parkingLot.longitude.toDouble()))
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))

                /* Add marker to map */
                this.map?.addMarker(marker)

                /* Move map camera to park */
                this.map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(parkingLot.latitude.toDouble(), parkingLot.longitude.toDouble()), 10.0f))

            }
        }
    }

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

        Log.i(TAG, "Map received!")

        this.map = map

        /* After receiving map, pin parking lot */
        setMapMarker()
    }

    override fun onLocationChangedListener(locationResult: LocationResult) {
        val location = locationResult.lastLocation
    }
}