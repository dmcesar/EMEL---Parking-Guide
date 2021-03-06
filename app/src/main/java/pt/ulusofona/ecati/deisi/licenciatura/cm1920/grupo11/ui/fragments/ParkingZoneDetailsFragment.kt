package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_parking_zone_details.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnParkingZoneDetailsListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.ParkingZoneDetailsViewModel

class ParkingZoneDetailsFragment : Fragment(), OnParkingZoneDetailsListener {

    private lateinit var viewModel: ParkingZoneDetailsViewModel

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        this.arguments?.let {

            val coordinates = it.getDoubleArray(EXTRA_PARK_COORDINATES)

            coordinates?.let { coords ->

                this.latitude = coords[0]
                this.longitude = coords[1]
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_zone_details, container, false)

        this.viewModel = ViewModelProviders.of(this).get(ParkingZoneDetailsViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.viewModel.registerListener(this)

        this.latitude?.let {lat ->

            this.longitude?.let { long ->

                this.viewModel.getInfo(lat, long)
            }
        }

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        super.onStop()
    }

    override fun onParkingZoneDetailsReceived(data: ParkingZoneResponse) {

        val type = (activity as Context).resources.getString(R.string.type) + ": " + data.type
        val schedule = (activity as Context).resources.getString(R.string.schedule) + ": " + data.schedule
        val observations = (activity as Context).resources.getString(R.string.observations) + ": " + if(data.observations != "null" || data.observations != null) {data.observations} else {(activity as Context).resources.getString(R.string.n_a)}
        val tariff = (activity as Context).resources.getString(R.string.tariff) + ": " + data.tariff

        parking_type.text = type
        parking_schedule.text = schedule
        parking_observations.text = observations
        parking_tariff.text = tariff
    }

    override fun onNoConnectivity() {

        Snackbar.make(parking_zone_layout, R.string.no_connection, Snackbar.LENGTH_LONG).show()
    }
}
