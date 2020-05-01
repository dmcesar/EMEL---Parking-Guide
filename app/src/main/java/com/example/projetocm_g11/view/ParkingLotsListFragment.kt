package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.adapters.ParkingLotPortraitAdapter
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.*
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.view.ParkingLotsListFragment.ParkingLot"

class ParkingLotsListFragment : Fragment(), OnDataReceived, OnTouchEvent {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var navigationListener: OnNavigateToFragment? = null

    private lateinit var viewModel: ParkingLotsViewModel

    @OnClick(R.id.button_filter)
    fun showPopupFilter() {

        /* Generate filters fragment */
        val filtersFragment = ParkingLotsFiltersFragment()

        /* Notify observer (MainActivity) to navigate to created fragment */
        this.navigationListener?.onNavigateToFragment(filtersFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.i(TAG, "onCreateView() called")

        /* Inflate layout */
        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        /* Bind fragment to view */
        ButterKnife.bind(this, view)

        /* Set layout for RecycleView */
        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        /* Obtain ViewModel */
        this.viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        return view
    }

    override fun onStart() {

        Log.i(TAG, "onStart() called")

        /* Set init value for list */
        this.viewModel.parkingLots.let { updateAdapter(ArrayList(it)) }

        /* This listening for ViewModel requests to update UI */
        this.viewModel.registerListeners(this)

        /* Activity listening for navigation requests (onClick item and onClick filters) */
        this.navigationListener = activity as OnNavigateToFragment

        super.onStart()
    }

    override fun onDestroy() {

        Log.i(TAG, "onDestroy() called")

        /* Unregister as observable from navigationListener */
        this.navigationListener = null

        /* Unregister as observer from viewModel */
        this.viewModel.unregisterListeners()

        super.onDestroy()
    }

    /* Updates RecycleView based on data received from ViewModel.
    * Sets adapter layout based on screen orientation. */
    private fun updateAdapter(data: ArrayList<ParkingLot>) {

        Log.i(TAG, "updateAdapter() called")

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

    /* Observer action triggered by ViewModel.
    * Calls onDataChanged with data as ArrayList<ParkingLot>. */
    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "onDataReceived() called")

        data?.let{ updateAdapter(it as ArrayList<ParkingLot>) }
    }

    override fun onSwipeEvent(data: Any?, direction: Int) {

        Log.i(TAG, "onSwipeEvent() called")

        data?.let {

            /* Swiped right: Show map */
            if (direction == 1) {

                /* Create NavigationFragment */
                val navigationFragment = NavigationFragment()

                /* Create arguments */
                val args = Bundle()
                args.putParcelable(EXTRA_PARKING_LOT, it as ParkingLot)
                navigationFragment.arguments = args

                /* Notify observer (MainActivity) to navigate to created fragment */
                this.navigationListener?.onNavigateToFragment(navigationFragment)
            }

            /* Swiped left: Tag/Untag as favorite */
            else this.viewModel.toggleFavorite(data as String)
        }
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        Log.i(TAG, "onClickEvent() called")

        data?.let {

            /* Create ParkingLotInfoFragment */
            val itemDetail = ParkingLotInfoFragment()

            /* Create arguments */
            val args = Bundle()
            args.putParcelable(EXTRA_PARKING_LOT, it as ParkingLot)
            itemDetail.arguments = args

            /* Notify observer (MainActivity) to navigate to created fragment */
            this.navigationListener?.onNavigateToFragment(itemDetail)
        }
    }
}