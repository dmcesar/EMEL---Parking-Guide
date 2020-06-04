package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_parking_lot_info.view.*

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener
import java.math.RoundingMode
import java.text.SimpleDateFormat

const val EXTRA_PARK_COORDINATES = "com.example.projetocm-g11.view.ParkingPlaceInfoFragment.PARK_COORDINATES"

class ParkingLotDetailsFragment : Fragment() {

    private var listener: OnNavigationListener? = null
    private lateinit var parkingLot: ParkingLot

    @OnClick(R.id.button_go_map)
    fun onClickSetCourse() {

        val latitude = this.parkingLot.latitude
        val longitude = this.parkingLot.longitude

        val intent = Intent(Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude"))
        startActivity(intent)
    }

    @OnClick(R.id.button_go_info)
    fun onClickGoInfo() {

        val coordinates = doubleArrayOf(this.parkingLot.latitude.toDouble(), this.parkingLot.longitude.toDouble())

        val args = Bundle()
        args.putDoubleArray(EXTRA_PARK_COORDINATES, coordinates)

        this.listener?.onNavigateToParkingZoneDetails(args)
    }

    private fun registerListener() {

        this.listener = activity as OnNavigationListener
    }

    private fun unregisterListener() {

        this.listener = null
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lot_info, container, false)

        ButterKnife.bind(this, view)

        this.parkingLot = this.arguments?.getParcelable(EXTRA_PARKING_LOT) as ParkingLot

        val type = if(this.parkingLot.getTypeEnum() == Type.UNDERGROUND)
            (activity as Context).resources.getString(R.string.type_underground)
        else (activity as Context).resources.getString(R.string.type_surface)

        val active = if(this.parkingLot.active == 1)
            (activity as Context).resources.getString(R.string.park_open)
        else (activity as Context).resources.getString(R.string.park_closed)

        val occupancy = "${this.parkingLot.occupancy} / ${this.parkingLot.maxCapacity}"

        val lastUpdatedAt = "${ (activity as Context).resources.getString(R.string.last_updated_at) }: " +
                SimpleDateFormat("dd-MM-yyyy").format(this.parkingLot.lastUpdatedAt)

        val state = when {

            parkingLot.getCapacityPercent() == 100 -> {

                (activity as Context).resources.getString(R.string.state_full)
            }

            parkingLot.getCapacityPercent() >= 90 -> {

                (activity as Context).resources.getString(R.string.state_potentially_full)
            }

            else -> (activity as Context).resources.getString(R.string.state_free)
        }

        val capacityPercent = this.parkingLot.getCapacityPercent().toString() + "%"

        val distance = context?.resources?.getString(R.string.distance) + ": " + (this.parkingLot.distanceToUser / 1000).toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString() + context?.resources?.getString(R.string.kilometers)

        view.park_capacity_bar.progress = this.parkingLot.getCapacityPercent()
        view.park_capacity_text.text = capacityPercent
        view.park_occupancy_state.text = state
        view.park_name.text = this.parkingLot.name
        view.park_type.text = type
        view.park_availability.text = active
        view.park_distance.text = distance
        view.last_updated_at.text = lastUpdatedAt
        view.park_occupancy?.text = occupancy

        if(parkingLot.active == 1) {
            view.park_availability.setTextColor(ContextCompat.getColor(activity as Context, R.color.forest_green))

        } else view.park_availability.setTextColor(ContextCompat.getColor(activity as Context, R.color.amber))

        if(!screenRotated(savedInstanceState)) {

            /* Create map fragment */
            val map = MapFragment()

            /* Set list with parking lot as argument */
            val args = Bundle()
            args.putParcelableArrayList(
                EXTRA_DATA,
                arrayListOf(this.parkingLot)
            )
            map.arguments = args

            /* Place fragment in FrameLayout */
            val transition = this.childFragmentManager.beginTransaction()
            transition.replace(R.id.map_frame, map)
            transition.addToBackStack(null)
            transition.commit()
        }

        return view
    }

    override fun onStart() {

        registerListener()

        super.onStart()
    }

    override fun onStop() {

        unregisterListener()

        super.onStop()
    }
}