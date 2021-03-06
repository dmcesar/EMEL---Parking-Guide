package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnMapMarkersReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.MapViewModel

class MapFragment : Fragment(), OnMapReadyCallback,
    OnLocationChangedListener, GoogleMap.OnMarkerClickListener, OnMapMarkersReceivedListener {

    private lateinit var viewModel: MapViewModel

    private var map: GoogleMap? = null

    private var userMarker: Marker? = null

    private val markersParkingLots = HashMap<LatLng, ParkingLot>()
    private var zoomOnUser: Boolean = true

    private var listener: OnNavigationListener? = null

    private fun handleArgs() {

        this.arguments?.let { args ->

            val parkingLots = args.getParcelableArrayList<ParkingLot>(EXTRA_DATA)

            parkingLots?.let { list ->

                /* If list size is 1 (parking lot details), hide legend button */
                if(list.size == 1) {

                    fab_toggle_legend?.visibility = View.GONE
                    this.zoomOnUser = false
                }

                this.viewModel.getParkingLotPins(list)
            }
        }
    }

    private fun pinUser(coordinates: LatLng) {

        this.context?.let { context ->

            val userPin = MarkerOptions()
                .title("User")
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

                if(this.zoomOnUser) {

                    this.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12.0f))
                }
            }
        }
    }

    private fun pinParkingLot(parkingLot: ParkingLot, markerOptions: MarkerOptions) {

        /* Add marker to map */
        this.map?.addMarker(markerOptions)

        this.markersParkingLots[markerOptions.position] = parkingLot

        if(!this.zoomOnUser) {

            this.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.position, 12.0f))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.map_view.onCreate(savedInstanceState)

        this.viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)

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

        if(parentFragment is OnNavigationListener) {

            this.listener = parentFragment as OnNavigationListener
        }

        this.viewModel.registerListener(this)

        FusedLocation.registerMapListener(this)
        map_view.getMapAsync(this)
        map_view.onResume()

        super.onStart()
    }

    override fun onStop() {

        this.listener = null
        this.viewModel.unregisterListener()

        FusedLocation.unregisterMapListener()

        super.onStop()
    }

    override fun onMapReady(map: GoogleMap?) {

        this.map = map

        this.map?.setOnMarkerClickListener(this)

        /* After receiving map, pin parking lots */
        handleArgs()
    }

    override fun onLocationChanged(locationResult: LocationResult) {

        val location = locationResult.lastLocation

        pinUser(Extensions.toLatLng(location))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        marker?.let {

            if (it.title.isNotEmpty() && it.title != "User") {

                val parkingLot = this.markersParkingLots[it.position]

                val args = Bundle()
                args.putParcelable(EXTRA_PARKING_LOT, parkingLot)

                this.listener?.onNavigateToParkingLotDetails(args)
            }
        }

        return false
    }

    override fun onMapMarkersReceived(markers: HashMap<ParkingLot, MarkerOptions>) {

        for((parkingLot, marker) in markers) {

            pinParkingLot(parkingLot, marker)
        }
    }
}