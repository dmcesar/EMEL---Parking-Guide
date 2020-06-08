package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.ParkingLotLandscapeAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.ParkingLotPortraitAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA_FROM_REMOTE
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.FiltersAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnTouchListener
import kotlin.collections.ArrayList

class ParkingLotsListFragment : Fragment(),
    OnTouchListener {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var listener: OnTouchListener? = null

    private var parkingLots: ArrayList<ParkingLot>? = null
    private var filters: ArrayList<Filter>? = null
    private var dataIsFromRemote: Boolean? = null
    private var dataFetchedDuringSplash: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.arguments?.let {

            this.parkingLots = it.getParcelableArrayList(EXTRA_DATA)
            this.filters = it.getParcelableArrayList(EXTRA_FILTERS)
            this.dataIsFromRemote = it.getBoolean(EXTRA_DATA_FROM_REMOTE)
            this.dataFetchedDuringSplash = it.getBoolean(EXTRA_DATA_FETCHED_DURING_SPLASH)
        }

        this.arguments = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_parking_lots_list, container, false)
    }

    override fun onStart() {

        this.listener = parentFragment as OnTouchListener
        this.parkingLots?.let { initParkingLots(it) }
        this.filters?.let { initFilters(it) }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity as Context)

        this.dataIsFromRemote?.let { fromRemote ->

            /* If data is not from remote API */
            if(!fromRemote) {

                this.dataFetchedDuringSplash?.let { fromSplash ->

                    /* If data was read during splash screen */
                    if(fromSplash) {

                        /* Alert that data is potentially outdated */
                        AlertDialog.Builder(activity as Context)
                            .setTitle(R.string.outdatedDataTitle)
                            .setMessage(R.string.outdatedDataMessage)
                            .setNegativeButton(R.string.OK, null)
                            .show()
                    }

                    /* If data was read after splash screen */
                    else {

                        /* Save that in shared preferences */
                        sharedPreferences
                            .edit()
                            .putBoolean(EXTRA_DATA_FROM_REMOTE, false)
                            .apply()
                    }
                }
            }
        }

        super.onStart()
    }

    override fun onStop() {

        this.listener = null

        super.onStop()
    }

    /* Updates RecycleView based on received data and screen orientation */
    private fun initParkingLots(parkingLots: ArrayList<ParkingLot>) {

        parking_lots_list.layoutManager = LinearLayoutManager(activity as Context)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            parking_lots_list.adapter =
                ParkingLotPortraitAdapter(
                    this,
                    activity as Context,
                    R.layout.parking_lots_portrait_list_item,
                    parkingLots
                )

        } else {

            parking_lots_list.adapter =
                ParkingLotLandscapeAdapter(
                    this,
                    activity as Context,
                    R.layout.parking_lots_landscape_list_item,
                    parkingLots
                )
        }
    }

    private fun initFilters(filters: ArrayList<Filter>) {

        filters_list.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)
        filters_list.adapter = FiltersAdapter(this, activity as Context, R.layout.filters_list_item_portrait, filters)
    }

    override fun onSwipeLeftEvent(data: Any?) {

        this.listener?.onSwipeLeftEvent(data)
    }

    override fun onSwipeRightEvent(data: Any?) {

        this.listener?.onSwipeRightEvent(data)
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        this.listener?.onClickEvent(data)
    }
}