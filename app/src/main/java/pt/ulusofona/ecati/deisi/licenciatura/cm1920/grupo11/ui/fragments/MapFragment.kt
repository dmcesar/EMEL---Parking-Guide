package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.LOCATION_REQUEST_CODE
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions

class MapFragment : PermissionsFragment(LOCATION_REQUEST_CODE), OnMapReadyCallback,
    OnLocationChangedListener {

    private val TAG = MapFragment::class.java.simpleName

    private var map: GoogleMap? = null

    private var userMarker: Marker? = null

    private fun handleArgs() {

        this.arguments?.let { args ->

            val parkingLots = args.getParcelableArrayList<ParkingLot>(EXTRA_DATA)

            parkingLots?.let { list ->

                /* If list size is 1 (parking lot details), hide legend button */
                if(list.size == 1) {

                    fab_toggle_legend.visibility = View.GONE
                }

                list.forEach { p -> pinParkingLot(p) }

            }
        }
    }

    private fun pinUser(coordinates: LatLng) {

        this.context?.let { context ->

            val userPin = MarkerOptions()
                .position(coordinates)
                .icon(Extensions.bitmapDescriptorFromVector(context, R.drawable.ic_user_marker))

            var userMoved = true

            /* If there was a previous pin, check if user moved. If user moved, remove pin and add new one */
            this.userMarker?.let { userMarker ->

                if(userMarker.position == coordinates) {

                    userMoved = false

                } else {

                    userMarker.remove()
                }
            }

            /* If user moved, update marker and camera */
            if(userMoved) {

                this.userMarker = this.map?.addMarker(userPin)
                this.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12.0f))
            }
        }
    }

    private fun pinParkingLot(parkingLot: ParkingLot) {

        /* Create marker with park coordinates and name */
        val marker = MarkerOptions()
            .title(parkingLot.name)
            .position(LatLng(parkingLot.latitude.toDouble(), parkingLot.longitude.toDouble()))

        if(parkingLot.getCapacityPercent() < 90) {

            if(parkingLot.getTypeEnum() == Type.UNDERGROUND) {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_free_underground))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_free_underground_closed))
                }

            } else {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_free))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_free_closed))
                }
            }

        } else if(parkingLot.getCapacityPercent() in 90..99) {

            if(parkingLot.getTypeEnum() == Type.UNDERGROUND) {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_potentially_full_underground))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_potentially_full_underground_closed))
                }

            } else {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_potentially_full))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_potentially_full_closed))
                }
            }

        } else {

            if(parkingLot.getTypeEnum() == Type.UNDERGROUND) {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_full_underground))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_full_underground_closed))
                }

            } else {

                if(parkingLot.active == 1) {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_full))

                } else {

                    marker.icon(Extensions.bitmapDescriptorFromVector(activity as Context, R.drawable.ic_map_marker_full_closed))
                }
            }
        }

        /* Add marker to map */
        this.map?.addMarker(marker)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.map_view.onCreate(savedInstanceState)

        view.fab_toggle_legend.setOnClickListener {

            if(view.map_legend.visibility == View.GONE) {

                view.map_legend.visibility = View.VISIBLE

                view.fab_toggle_legend.setImageDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.ic_close))


            } else {

                view.map_legend.visibility = View.GONE

                view.fab_toggle_legend.setImageDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.ic_map))

            }
        }

        return view
    }

    override fun onStart() {

        super.onRequestPermissions(activity?.baseContext!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))

        super.onStart()
    }

    override fun onStop() {

        FusedLocation.unregisterGoogleMapListener()

        super.onStop()
    }

    override fun onRequestPermissionsSuccess() {

        Log.i(TAG, "OnRequestPermissionsSuccess")

        FusedLocation.registerGoogleMapListener(this)
        map_view.getMapAsync(this)
        map_view.onResume()
    }

    override fun onRequestPermissionsFailure() {

        Log.i(TAG, "OnRequestPermissionsFailure")
    }

    override fun onMapReady(map: GoogleMap?) {

        this.map = map

        /* After receiving map, pin parking lots */
        handleArgs()
    }

    override fun onLocationChanged(locationResult: LocationResult) {

        Log.i(TAG, "user location changed")

        val location = locationResult.lastLocation

        pinUser(Extensions.toLatLng(location))
    }
}