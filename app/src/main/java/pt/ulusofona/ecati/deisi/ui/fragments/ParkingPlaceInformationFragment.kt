package pt.ulusofona.ecati.deisi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import pt.ulusofona.ecati.deisi.R
import pt.ulusofona.ecati.deisi.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.ui.viewmodels.ParkingPlaceInformationViewModel

class ParkingPlaceInformationFragment : Fragment(),
    OnDataReceivedListener {

    private lateinit var viewModel: ParkingPlaceInformationViewModel

    private fun handleArgs() {

        if(this.arguments != null) {

            val coordinates = this.arguments?.getStringArray(EXTRA_PARK_COORDINATES)

            coordinates?.let {

                this.viewModel.getInfo(it[0].toDouble(), it[1].toDouble())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_place_information, container, false)

        this.viewModel = ViewModelProviders.of(this).get(ParkingPlaceInformationViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.viewModel.registerListener(this)

        handleArgs()

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        super.onStop()
    }

    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let {

            // TODO: Extra: manage data received from API
        }
    }
}
