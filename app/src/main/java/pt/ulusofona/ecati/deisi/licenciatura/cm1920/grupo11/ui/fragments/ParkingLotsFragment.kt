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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA_FROM_REMOTE
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.ParkingLotsNavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots.*
import kotlinx.android.synthetic.main.fragment_parking_lots.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.accelerometer.Accelerometer
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.accelerometer.OnAccelerometerEventListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnTouchListener

const val EXTRA_DATA_FETCHED_BEFORE = "pt.ulusofona.ecati.ParkingLotsListFragment.DATA_FETCHED_BEFORE"
const val EXTRA_PARKING_LOT = "pt.ulusofona.ecati.ParkingLotsListFragment.ParkingLot"

class ParkingLotsFragment : Fragment(),
    OnDataReceivedListener,
    OnTouchListener,
    OnAccelerometerEventListener {

    private val TAG = ParkingLotsFragment::class.java.simpleName

    private val QUEUED_FRAGMENT_KEY = "queued_fragment"

    private lateinit var viewModel: ParkingLotsViewModel

    private var listener: OnNavigationListener? = null

    /* Used when onDataReceived() is called by observable.
    * If value is 0, navigate to ListView fragment.
    * If value is 1, navigate to MapView fragment. */
    private var queuedFragment: Int = 0

    @OnClick(R.id.button_go_list_view)
    fun onClickGoListView() {

        /* Toggle app bar button's background */
        button_go_list_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button_selected)
        button_go_map_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button)

        queuedFragment = 0

        /* Request data from viewModel */
        this.viewModel.getAll()
    }

    @OnClick(R.id.button_go_map_view)
    fun onClickGoMapView() {

        /* Toggle app bar button's background */
        button_go_map_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button_selected)
        button_go_list_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button)

        queuedFragment = 1

        /* Request data from viewModel */
        this.viewModel.getAll()
    }

    @OnClick(R.id.button_filter)
    fun onClickGoFiltersFragment() {

        this.listener?.onNavigateToFiltersFragment()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.run { putInt(QUEUED_FRAGMENT_KEY, queuedFragment) }

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        this.queuedFragment = savedInstanceState?.getInt(QUEUED_FRAGMENT_KEY) ?: 0

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots, container, false)

        ButterKnife.bind(this, view)

        this.viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        /* Set action bar buttons' status */
        if(queuedFragment == 0) {

            view.button_go_list_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button_selected)
            view.button_go_map_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button)

        } else {

            view.button_go_map_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button_selected)
            view.button_go_list_view.background = ContextCompat.getDrawable(activity as Context, R.drawable.app_bar_button)
        }

        /* When fragment is recreated, navigate to list fragment */
        if(!screenRotated(savedInstanceState)) {

            /* If arguments where received */
            this.arguments?.let {

                /* Received data from MainActivity */
                val dataReceived: ArrayList<ParkingLot>? = it.getParcelableArrayList(
                    EXTRA_DATA
                )
                val dataFromRemote: Boolean = it.getBoolean(EXTRA_DATA_FROM_REMOTE)

                /* Clear arguments so no data is overwritten */
                this.arguments = null

                /* Navigate to ListView Fragment with received arguments */
                val args = Bundle()
                args.putParcelableArrayList(EXTRA_DATA, dataReceived)
                args.putBoolean(EXTRA_DATA_FROM_REMOTE, dataFromRemote)
                args.putBoolean(EXTRA_DATA_FETCHED_BEFORE, true)

                ParkingLotsNavigationManager.goToListFragment(childFragmentManager, args)
            } ?:

            /* If no arguments where received */
            kotlin.run {

                this.viewModel.getAll()
            }

        } else {

            /* Request data from ViewModel */
            this.viewModel.getAll()
        }

        return view
    }

    override fun onStart() {

        /* Register listeners */
        this.listener = (this.activity as OnNavigationListener)
        this.viewModel.registerListener(this)

        Accelerometer.registerParkingLotsListener(this)

        super.onStart()
    }

    override fun onStop() {

        /* Unregister listeners */
        this.listener = null
        this.viewModel.unregisterListener()

        Accelerometer.unregisterParkingLotsListener()

        super.onStop()
    }

    /* Receives parking lots list stored locally and navigates do queued fragment */
    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "ParkingLots received ${data?.size}")

        /* Create arguments */
        val args = Bundle()
        args.putParcelableArrayList(EXTRA_DATA, data as ArrayList<ParkingLot>)
        args.putBoolean(EXTRA_DATA_FROM_REMOTE, false)
        args.putBoolean(EXTRA_DATA_FETCHED_BEFORE, false)

        if(queuedFragment == 0) {

            ParkingLotsNavigationManager.goToListFragment(childFragmentManager, args)
        }

        else { ParkingLotsNavigationManager.goToMapFragment(childFragmentManager, args) }
    }

    override fun onSwipeLeftEvent(data: Any?) {

        this.viewModel.toggleFavorite(data as ParkingLot)
    }

    override fun onSwipeRightEvent(data: Any?) {

        data?.let {

            it as ParkingLot

            val latitude = it.latitude
            val longitude = it.longitude

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude"))

            startActivity(intent)
        }
    }

    override fun onClickEvent(data: Any?) {

        /* Create arguments with parking lot */
        val args = Bundle()
        args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)

        /* Notify observer to navigate to ParkingLotDetailsFragment with created args */
        this.listener?.onNavigateToParkingLotDetails(args)
    }

    override fun onAccelerometerEventListener() {

        this.viewModel.removeFilters()
    }
}
