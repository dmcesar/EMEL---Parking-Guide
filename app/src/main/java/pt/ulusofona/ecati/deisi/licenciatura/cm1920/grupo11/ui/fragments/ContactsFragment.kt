package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick

import kotlinx.android.synthetic.main.fragment_contacts.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R

class ContactsFragment : Fragment() {

    @OnClick(R.id.button_service_stations)
    fun onToggleServiceStations() {

        if(service_stations.visibility == View.VISIBLE) {

            service_stations.visibility = View.GONE

            button_service_stations.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            service_stations.visibility = View.VISIBLE

            button_service_stations.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_general_information)
    fun onToggleGeneralInformation() {

        if(general_information.visibility == View.VISIBLE) {

            general_information.visibility = View.GONE

            button_general_information.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            general_information.visibility = View.VISIBLE

            button_general_information.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_phone_general_information)
    fun onClickCallGeneralInformation() {

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:211163060")
        startActivity(intent)
    }

    @OnClick(R.id.button_removed_vehicles_park)
    fun onToggleRemovedVehiclesPark() {

        if(removed_vehicles_park.visibility == View.VISIBLE) {

            removed_vehicles_park.visibility = View.GONE

            button_removed_vehicles_park.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            removed_vehicles_park.visibility = View.VISIBLE

            button_removed_vehicles_park.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_removed_vehicles)
    fun onToggleRemovedVehicles() {

        if(removed_vehicles.visibility == View.VISIBLE) {

            removed_vehicles.visibility = View.GONE

            button_removed_vehicles.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            removed_vehicles.visibility = View.VISIBLE

            button_removed_vehicles.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.button_phone_removed_vehicles)
    fun onClickCallRemovedVehicles() {

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:217803131")
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        ButterKnife.bind(this, view)

        return view
    }
}