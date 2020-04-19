package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Resources
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.vehicles_list_item.*
import java.text.SimpleDateFormat

class VehicleFormFragment : Fragment() {

    private val TAG = VehicleFormFragment::class.java.simpleName

    private lateinit var viewModel: VehiclesListViewModel

    private var vehicle: Vehicle? = null

    @OnClick(R.id.button_submit_form)
    fun onClickSubmit() {

        Log.i(TAG, "Method onClickSubmit() called.")

        if(!validate()) {
            return
        }

        if(this.vehicle == null ) {

            this.vehicle = Vehicle(
                input_brand.text.toString(),
                input_model.text.toString(),
                input_plate.text.toString(),
                SimpleDateFormat("MM-yyyy").parse(input_plate_date.text.toString())
            )

            viewModel.registerVehicle(this.vehicle as Vehicle)

            Toast.makeText(activity as Context, "Vehicle registered!", Toast.LENGTH_LONG).show()

            activity?.onBackPressed()
        }

        else {

            viewModel.updateVehicle(this.vehicle as Vehicle)

            updateFormData()

            Toast.makeText(activity as Context, "Vehicle data updated!", Toast.LENGTH_LONG).show()
        }
    }

    /* Validates form based on type of form (Create or Update) and input patterns*/
    private fun validate(): Boolean {

        val licencePattern = "([A-Za-z]{2}|[0-9]{2})-([A-Za-z]{2}|[0-9]{2})-([A-Za-z]{2}|[0-9]{2})"
        val datePattern = "\\d{2}-\\d{4}"

        var validForm = true

        if(this.vehicle == null && input_brand.text.isEmpty()) {

            input_brand.error = "Mandatory field."
            validForm = false
        }

        else if(this.vehicle != null && input_brand.text.isNotEmpty()) {

            this.vehicle?.brand = input_brand.text.toString()
        }

        if(this.vehicle == null && input_model.text.isEmpty()) {

            input_model.error = "Mandatory field."
            validForm = false
        }

        else if(this.vehicle != null && input_model.text.isNotEmpty()) {

            this.vehicle?.model = input_model.text.toString()
        }

        if(this.vehicle == null && !input_plate.text.matches(Regex(licencePattern))
            || this.vehicle != null && input_plate.text.isNotEmpty() && !input_plate.text.matches(Regex(licencePattern))) {

            input_plate.error = "Invalid plate format."
            validForm = false
        }

        else if(this.vehicle != null && input_plate.text.matches(Regex(licencePattern))) {
            this.vehicle?.plate = input_plate.text.toString()
        }

        if(this.vehicle == null && !input_plate_date.text.matches(Regex(datePattern))
            || this.vehicle != null && input_plate_date.text.isNotEmpty() && !input_plate_date.text.matches(Regex(datePattern))) {

            input_plate_date.error = "Invalid date format."
            validForm = false
        }

        else if(this.vehicle != null && input_plate_date.text.matches(Regex(datePattern))) {

            this.vehicle?.setDate(SimpleDateFormat("MM-yyyy").parse(input_plate_date.text.toString()))
        }

        return validForm
    }

    private fun updateFormData() {

        brand?.text = vehicle?.brand
        model?.text = vehicle?.model
        plate?.text = vehicle?.plate
        plate_date?.text = vehicle?.getDate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View?

        if(arguments == null) {

            view = inflater.inflate(R.layout.fragment_vehicle_form, container, false)

        } else {

            view = inflater.inflate(R.layout.fragment_vehicle_form_edit, container, false)

            this.vehicle = this.arguments?.getParcelable(EXTRA_VEHICLE)
        }

        ButterKnife.bind(this, view)

        viewModel = ViewModelProviders.of(this).get(VehiclesListViewModel::class.java)

        return view
    }

    override fun onStart() {

        input_plate_date.addTextChangedListener(object : TextWatcher {

            var ignore = false
            var previous = 0

            var appendDash = false
            var limitSize = false

            override fun afterTextChanged(p0: Editable?) {

                if(!ignore) {

                    if (appendDash) {

                        ignore = true
                        p0?.append("-")
                        appendDash = false
                    }

                    if(limitSize) {

                        ignore = true

                        p0?.delete(7, 8)
                        limitSize = false
                    }

                    ignore = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                Log.i(TAG, "$count - $before")

                if (start == 1 && count > before) {
                    appendDash = true
                }

                if (start == 7) {
                    limitSize = true
                }
            }
        })

        if(this.vehicle != null) {

            updateFormData()

            button_delete.setOnClickListener {

                viewModel.deleteVehicle( (this.vehicle as Vehicle).uuid)

                Toast.makeText(activity as Context, "Vehicle deleted.", Toast.LENGTH_LONG).show()

                activity?.onBackPressed()
            }

            val res = resources

            val pageTitle = res.getString(R.string.update_vehicle)
            val buttonTitle = res.getString(R.string.update)

            form_name.text = pageTitle
            button_submit_form.text = buttonTitle
        }

        super.onStart()
    }
}