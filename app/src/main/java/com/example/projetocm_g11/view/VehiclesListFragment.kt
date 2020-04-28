package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
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

        /* Inflate layout */
        val view =  inflater.inflate(R.layout.fragment_vehicles_list, container, false)

        /* Set layout for RecycleView according to screen orientation */
        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            view.vehicles.layoutManager = LinearLayoutManager(activity as Context)

        }
        else view.vehicles.layoutManager = GridLayoutManager(activity as Context, 2)

        /* Set FAB onClickListener */
        view.fab.setOnClickListener {

            /* Create an empty vehicle form */
            val emptyFormFragment = VehicleFormFragment()

            // Notify MainActivity to navigate to Fragment
            this.listener?.onNavigateToFragment(emptyFormFragment)
        }

        /* Obtain ViewModel*/
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
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "onDataReceived() called")

        data?.let {

            if(it.size > 0) {

                empty_list_text.visibility = View.GONE

            } else empty_list_text.visibility = View.VISIBLE

            vehicles.adapter = VehicleAdapter(this, activity as Context, R.layout.vehicles_list_item, it as ArrayList<Vehicle>)
        }
    }

    override fun onClickEvent(data: Any?) {

        Log.i(TAG, "onClickEvent() called")

        data?.let {

            if(it is Vehicle) {

                val args = Bundle()
                args.putParcelable(EXTRA_VEHICLE, it)

                val editForm = VehicleFormFragment()
                editForm.arguments = args

                this.listener?.onNavigateToFragment(editForm)

            } else { viewModel.deleteVehicle(it as String) }
        }
    }
}
