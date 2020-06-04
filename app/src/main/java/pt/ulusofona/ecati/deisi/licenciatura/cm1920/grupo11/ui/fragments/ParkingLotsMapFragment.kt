package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_parking_lots.*
import kotlinx.android.synthetic.main.fragment_parking_lots_map.*
import kotlinx.android.synthetic.main.fragment_parking_lots_map.view.*

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA_FROM_REMOTE
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.FiltersAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnClickListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener

class ParkingLotsMapFragment : Fragment(), OnNavigationListener, OnClickListener {

    private val TAG = ParkingLotsMapFragment::class.java.simpleName

    private var parkingLots: ArrayList<ParkingLot>? = null
    private var filters: ArrayList<Filter>? = null
    private var dataIsFromRemote: Boolean? = null

    private var listener: OnNavigationListener? = null
    private var onClickListener: OnClickListener? = null

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.arguments?.let {

            this.parkingLots = it.getParcelableArrayList(EXTRA_DATA)
            this.filters = it.getParcelableArrayList(EXTRA_FILTERS)
            this.dataIsFromRemote = it.getBoolean(EXTRA_DATA_FROM_REMOTE)
        }

        this.arguments = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_map, container, false)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            view.filters_list.layoutManager =
                LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)

        } else view.filters_list.layoutManager = LinearLayoutManager(activity as Context)

        if(!screenRotated(savedInstanceState)) {

            /* Create map fragment */
            val map = MapFragment()

            /* Set list with parking lot as argument */
            val args = Bundle()
            args.putParcelableArrayList(
                EXTRA_DATA,
                parkingLots
            )
            map.arguments = args

            /* Place fragment in FrameLayout */
            val transition = this.childFragmentManager.beginTransaction()
            transition.replace(R.id.navigation_map_frame, map)
            transition.addToBackStack(null)
            transition.commit()
        }

        return view
    }

    override fun onStart() {

        if(parentFragment is OnNavigationListener) {

            this.listener = parentFragment as OnNavigationListener
        }

        if(parentFragment is OnClickListener) {

            this.onClickListener = parentFragment as OnClickListener
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity as Context)

        this.dataIsFromRemote?.let { fromRemote ->

            /* If data is from remove API */
            if(fromRemote) {
                /* If previously batch of data was outdated (from local) */
                val previousBatchOfDataWasUpdated = sharedPreferences
                    .getBoolean(EXTRA_DATA_FROM_REMOTE, false)

                if(!previousBatchOfDataWasUpdated) {

                    sharedPreferences
                        .edit()
                        .putBoolean(EXTRA_DATA_FROM_REMOTE, true)
                        .apply()

                    Snackbar.make(parking_lots_frame, (parentFragment?.activity as Context).resources.getString(R.string.data_updated_after_connection_was_lost), Snackbar.LENGTH_LONG)
                        .show()

                } else {

                    Log.i(TAG, "Data is updated and connection has not been lost!")
                }
            }
        }

        this.filters?.let {

            if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

                filters_list.adapter =
                    FiltersAdapter(
                        this,
                        activity as Context,
                        R.layout.filters_list_item_portrait,
                        it
                    )

            } else {

                filters_list.adapter =
                    FiltersAdapter(
                        this,
                        activity as Context,
                        R.layout.filters_list_item_landscape,
                        it
                    )
            }
        }

        super.onStart()
    }

    override fun onStop() {

        this.listener = null
        this.onClickListener = null

        super.onStop()
    }

    override fun onNavigateToParkingLotDetails(args: Bundle) {

        this.listener?.onNavigateToParkingLotDetails(args)
    }

    override fun onNavigateToFiltersFragment() {}

    override fun onNavigateToVehicleForm(args: Bundle?) {}

    override fun onNavigateToParkingZoneDetails(args: Bundle) {}

    override fun onClickEvent(data: Any?) {

        this.onClickListener?.onClickEvent(data)
    }
}
