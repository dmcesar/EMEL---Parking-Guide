package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemSelected

import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_contacts.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.ContactsViewModel

class ContactsFragment : Fragment(), OnDataReceivedListener {

    private val TAG = ContactsFragment::class.java.simpleName
    private var selectedPlate = ""

    private lateinit var viewModel: ContactsViewModel

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

    @OnClick(R.id.button_send_message)
    fun onClickSendMessage() {

        if(this.selectedPlate.isNotEmpty()) {

            val uri = Uri.parse("smsto:3838")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "Reboque " + this.selectedPlate)
            startActivity(intent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        ButterKnife.bind(this, view)

        view.vehicles_spinner.onItemSelectedListener = object: OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                parent?.let {

                    selectedPlate = it.getItemAtPosition(position).toString()

                    Log.i(TAG, selectedPlate)
                }
            }

        }

        this.viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.viewModel.registerListener(this)
        this.viewModel.getAll()

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data as ArrayList<String>

        val spinnerAdapter = ArrayAdapter(
            activity as Context,
            R.layout.spinner_text,
            data
        )

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        vehicles_spinner.adapter = spinnerAdapter

        vehicles_spinner.selectedItem?.let {

            this.selectedPlate = it.toString()
        }
    }
}