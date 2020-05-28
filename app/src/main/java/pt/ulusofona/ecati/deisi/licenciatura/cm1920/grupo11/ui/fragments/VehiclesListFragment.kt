package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

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
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.VehicleAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnClickListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.VehiclesListViewModel

import kotlinx.android.synthetic.main.fragment_vehicles_list.*
import kotlinx.android.synthetic.main.fragment_vehicles_list.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener

const val EXTRA_VEHICLE = "pt.ulusofona.ecati.VehiclesListFragment.VEHICLE"

class VehiclesListFragment : Fragment(),
    OnDataReceivedListener,
    OnClickListener {

    private val TAG = VehiclesListFragment::class.java.simpleName

    private lateinit var viewModel: VehiclesListViewModel

    private var listener: OnNavigationListener? = null

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

            /* Notify MainActivity to navigate to empty VehicleFormFragment */
            this.listener?.onNavigateToVehicleForm(null)
        }

        /* Obtain ViewModel*/
        viewModel = ViewModelProviders.of(this).get(VehiclesListViewModel::class.java)

        return view
    }

    override fun onStart() {

        /* Set init value for list */
        this.viewModel.vehicles.let { onDataChanged(it) }

        this.viewModel.registerListener(this)

        this.listener = activity as OnNavigationListener

        this.viewModel.read()

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        this.listener = null

        super.onStop()
    }

    private fun onDataChanged(data: ArrayList<Vehicle>) {

        if(data.size > 0) {

            empty_list_text.visibility = View.GONE

        } else empty_list_text.visibility = View.VISIBLE

        vehicles.adapter =
            VehicleAdapter(
                this,
                activity as Context,
                R.layout.vehicles_list_item,
                data
            )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "onDataReceived() called")

        data?.let { onDataChanged(it as ArrayList<Vehicle>) }
    }

    override fun onClickEvent(data: Any?) {

        Log.i(TAG, "onClickEvent() called")

        data?.let {

            if(it is Vehicle) {

                val args = Bundle()
                args.putParcelable(EXTRA_VEHICLE, it)

                /* Notify MainActivity to navigate to empty VehicleFormFragment */
                this.listener?.onNavigateToVehicleForm(args)

            } else { viewModel.delete(it as String) }
        }
    }
}