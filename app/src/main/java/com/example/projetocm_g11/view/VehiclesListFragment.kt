package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetocm_g11.*
import com.example.projetocm_g11.adapters.VehicleAdapter
import com.example.projetocm_g11.domain.data.Vehicle
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.viewmodel.VehiclesListViewModel

import kotlinx.android.synthetic.main.fragment_vehicles_list.*
import kotlinx.android.synthetic.main.fragment_vehicles_list.view.*
import kotlinx.android.synthetic.main.fragment_vehicles_list.view.vehicles

const val EXTRA_VEHICLE = "com.example.projetocm_g11.view.VehiclesListFragment.VEHICLE"

class VehiclesListFragment : Fragment(), OnDataReceived, OnClickEvent {

    private val TAG = VehiclesListFragment::class.java.simpleName

    private lateinit var viewModel: VehiclesListViewModel

    private var listener: OnNavigateToFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_vehicles_list, container, false)

        view.vehicles.layoutManager = LinearLayoutManager(activity as Context)

        view.fab.setOnClickListener {

            // Notify MainActivity to navigate to VehicleFormFragment
            this.listener?.onNavigateToFragment(VehicleFormFragment())

            /*

            // Inflate popup view using inflater
            val popupView = inflater.inflate(R.layout.popup_new_vehicle, null)

            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(popupView, width, height)

            // Close popup when clicked outside
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true

            // Finally, show the popup window on top of the vehicles list
            TransitionManager.beginDelayedTransition(vehicles)
            popupWindow.showAtLocation(vehicles, Gravity.CENTER, 0, 0)

             */
        }

        viewModel = ViewModelProviders.of(this).get(VehiclesListViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.viewModel.registerListener(this)
        this.listener = activity as OnNavigateToFragment
        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()
        this.listener = null
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        Log.i(TAG, "onDataReceived triggered.")

        if(list.size > 0) {

            empty_list_text.visibility = View.GONE

        } else empty_list_text.visibility = View.VISIBLE

        vehicles.adapter = VehicleAdapter(this, activity as Context, R.layout.vehicles_list_item, list as ArrayList<Vehicle>)
    }

    override fun onClickEvent(data: Any) {

        if(data is Vehicle) {

            val args = Bundle()
            args.putParcelable(EXTRA_VEHICLE, data)

            val editForm = VehicleFormFragment()
            editForm.arguments = args

            this.listener?.onNavigateToFragment(editForm)
        }

        else if(data is String) {

            viewModel.deleteVehicle(data)
        }
    }
}
