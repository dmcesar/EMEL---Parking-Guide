package com.example.projetocm_g11.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.ui.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.ui.adapters.ParkingLotPortraitAdapter
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.ui.activities.EXTRA_PARKING_LOTS
import com.example.projetocm_g11.ui.activities.EXTRA_UPDATED
import com.example.projetocm_g11.ui.listeners.*
import com.example.projetocm_g11.ui.viewmodels.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.ui.fragments.ParkingLotsListFragment.ParkingLot"

class ParkingLotsListFragment : Fragment(), OnDataReceived, OnTouchEvent {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var navigationListener: OnNavigateToFragment? = null

    private lateinit var viewModel: ParkingLotsViewModel

    private fun handleArgs() {

        val parkingLots = this.arguments?.getParcelableArrayList<ParkingLot>(EXTRA_PARKING_LOTS)
        val updated = this.arguments?.getBoolean(EXTRA_UPDATED)

        parkingLots?.let { onDataChanged(it) }
        Log.i(TAG, "Updated: " + updated.toString())
        // TODO: Notify if data outdated

        this.arguments = null
    }

    @OnClick(R.id.button_filter)
    fun showPopupFilter() {

        /* Generate filters fragment */
        val filtersFragment = ParkingLotsFiltersFragment()

        /* Notify MainActivity to navigate to Fragment */
        this.navigationListener?.onNavigateToFragment(filtersFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /* Inflate layout */
        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        /* Bind fragment to view */
        ButterKnife.bind(this, view)

        /* Set layout for RecycleView*/
        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        /* Obtain ViewModel*/
        this.viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        return view
    }

    override fun onStart() {

        /* Activity listening for navigation requests (onClick item and onClick filters) */
        this.navigationListener = activity as OnNavigateToFragment

        /* This listening for ViewModel requests to update UI */
        this.viewModel.registerListener(this)

        this.viewModel.parkingLots.let { onDataChanged(it) }

        if(this.arguments == null) {

            Log.i(TAG, "No arguments to read from")

            this.viewModel.getAll()

        } else {

            Log.i(TAG, "Arguments passed")

            /* Obtain arguments, init list and notify if data is potentially outdated */
            handleArgs()
        }

        super.onStart()
    }

    override fun onStop() {

        this.navigationListener = null

        this.viewModel.unregisterListener()

        super.onStop()
    }

    /* Updates RecycleView based on received data and screen orientation */
    private fun onDataChanged(data: ArrayList<ParkingLot>) {

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            parking_lots.adapter = ParkingLotPortraitAdapter(
                this,
                activity as Context,
                R.layout.parking_lots_portrait_list_item,
                data
            )

        } else {

            parking_lots.adapter = ParkingLotLandscapeAdapter(
                this,
                activity as Context,
                R.layout.parking_lots_landscape_list_item,
                data
            )
        }
    }

    /* Action triggered when ViewModel requests to update UI */
    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let{ onDataChanged(it as ArrayList<ParkingLot>) }
    }

    override fun onSwipeEvent(data: Any?, direction: Int) {

        /* Swiped right: Show map */
        if(direction == 1) {

            val args = Bundle()

            args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)

            val navigationFragment =
                NavigationFragment()
            navigationFragment.arguments = args

            this.navigationListener?.onNavigateToFragment(navigationFragment)
        }

        /* Swiped left: Tag/Untag as favorite */
        else {

            this.viewModel.toggleFavorite(data as ParkingLot)
        }
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        /* Generate details Fragment */
        val itemDetail =
            ParkingLotInfoFragment()

        /* Add arguments */
        val args = Bundle()
        data?.let { args.putParcelable(EXTRA_PARKING_LOT, it as ParkingLot) }
        itemDetail.arguments = args

        /* Notify observer to add new Fragment */
        this.navigationListener?.onNavigateToFragment(itemDetail)
    }
}