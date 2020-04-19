package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.viewmodel.VehiclesListViewModel
import kotlinx.android.synthetic.main.fragment_vehicle_form.*
import java.text.SimpleDateFormat

class VehicleFormFragment : Fragment() {

    private lateinit var viewModel: VehiclesListViewModel

    private val TAG = VehicleFormFragment::class.java.simpleName

    @OnClick(R.id.button_submit_form)
    fun onClickSubmit() {

        Log.i(TAG, "Method onClickSubmit() called.")

        if(!validate()) {
            return
        }

        val vehicle = Vehicle(
            input_brand.text.toString(),
            input_model.text.toString(),
            input_plate.text.toString(),
            SimpleDateFormat("MM-yyyy").parse(input_plate_date.text.toString())
        )

        viewModel.registerVehicle(vehicle)

        Toast.makeText(activity as Context, "Vehicle registered!", Toast.LENGTH_SHORT).show()
    }

    private fun validate(): Boolean {

        val licencePattern = "([A-Za-z]{2}|[0-9]{2})-([A-Za-z]{2}|[0-9]{2})-([A-Za-z]{2}|[0-9]{2})"
        val dateFormat = "\\d{2}-\\d{4}"

        var validForm = true

        if(input_brand.text.isEmpty()) {

            input_brand.error = "Mandatory field."
            validForm = false
        }

        if(input_model.text.isEmpty()) {

            input_model.error = "Mandatory field."
            validForm = false
        }

        if(input_plate.text.isEmpty() || !input_plate.text.matches(Regex(licencePattern))) {

            input_plate.error = "Invalid plate format."
            validForm = false
        }

        if(input_plate_date.text.isEmpty() || !input_plate_date.text.matches(Regex(dateFormat))) {

            input_plate_date.error = "Invalid date format."
            validForm = false
        }

        return validForm
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View?

        view = if(arguments == null) {

            inflater.inflate(R.layout.fragment_vehicle_form, container, false)

        } else inflater.inflate(R.layout.fragment_vehicle_form_edit, container, false)

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(VehiclesListViewModel::class.java)

        return view
    }
}