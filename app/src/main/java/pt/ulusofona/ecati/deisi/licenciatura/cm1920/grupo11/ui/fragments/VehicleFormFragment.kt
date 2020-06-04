package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
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

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.VehiclesListViewModel
import kotlinx.android.synthetic.main.fragment_vehicle_form.*
import kotlinx.android.synthetic.main.fragment_vehicle_form.button_submit_form
import kotlinx.android.synthetic.main.fragment_vehicle_form.form_name
import kotlinx.android.synthetic.main.fragment_vehicle_form.input_brand
import kotlinx.android.synthetic.main.fragment_vehicle_form.input_model
import kotlinx.android.synthetic.main.fragment_vehicle_form.input_plate
import kotlinx.android.synthetic.main.fragment_vehicle_form.input_plate_date
import kotlinx.android.synthetic.main.fragment_vehicle_form_edit.*
import kotlinx.android.synthetic.main.vehicles_list_item.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle
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

            this.vehicle =
                Vehicle(
                    brand = input_brand.text.toString(),
                    model = input_model.text.toString(),
                    plate = input_plate.text.toString(),
                    plateDate = SimpleDateFormat("MM-yyyy").parse(input_plate_date.text.toString())
                )

            viewModel.insert(this.vehicle as Vehicle)

            Toast.makeText(activity as Context, "Vehicle registered!", Toast.LENGTH_LONG).show()

            activity?.onBackPressed()
        }

        else {

            viewModel.update(this.vehicle as Vehicle)

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

        val brandText = (activity as Context).resources.getString(R.string.enter_brand) + ": " + vehicle?.brand
        val modelText = (activity as Context).resources.getString(R.string.enter_model) + ": " + vehicle?.model
        val plateText = (activity as Context).resources.getString(R.string.enter_plate) + ": " + vehicle?.plate
        val plateDateText = (activity as Context).resources.getString(R.string.enter_plate_date) + ": " + SimpleDateFormat("MM-yyyy").format(vehicle?.plateDate)

        brand?.text = brandText
        model?.text = modelText
        plate?.text = plateText
        plate_date?.text = plateDateText
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

        this.vehicle?.let {

            input_brand.setText(it.brand)
            input_model.setText(it.model)
            input_plate.setText(it.plate)
            input_plate_date.setText(SimpleDateFormat("MM-yyyy").format(it.plateDate))
        }

        input_plate_date.addTextChangedListener(object : TextWatcher {

            var ignore = false

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

            val res = resources

            val pageTitle = res.getString(R.string.update_vehicle)
            val buttonTitle = res.getString(R.string.update)

            form_name.text = pageTitle
            button_submit_form.text = buttonTitle
        }

        super.onStart()
    }
}