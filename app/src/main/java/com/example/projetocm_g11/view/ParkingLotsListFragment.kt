package com.example.projetocm_g11.view

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
import com.example.projetocm_g11.interfaces.OnDataReceived

import com.example.projetocm_g11.R
import com.example.projetocm_g11.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.adapters.ParkingLotPortraitAdapter
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnFiltersListReceived
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.view.ParkingLotsListFragment.ParkingLot"
const val EXTRA_PARKING_LOTS_LIST_SIZE = "com.example.projetocm_g11.view.ParkingLotsListFragment.ListSize"
const val EXTRA_FILTERS_LIST = "com.example.projetocm_g11.view.ParkingLotsListFragment.FiltersList"

class ParkingLotsListFragment : Fragment(), OnDataReceived, OnFiltersListReceived, OnClickEvent {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var navigationListener: OnNavigateToFragment? = null

    private lateinit var viewModel: ParkingLotsViewModel

    @OnClick(R.id.button_filter)
    fun showPopupFilter() {

        /* Generate filters fragment */
        val filtersFragment = ParkingLotsFiltersFragment()

        /* Register this Fragment as observer for new filtersFragment's filters' list */
        filtersFragment.registerListener(this)

        val args = Bundle()

        this.viewModel.filters.let { args.putParcelableArrayList(EXTRA_FILTERS_LIST, it) }
        this.viewModel.parkingLots.size.let { args.putInt(EXTRA_PARKING_LOTS_LIST_SIZE, it) }

        filtersFragment.arguments = args

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /* Set init value for list */
        this.viewModel.parkingLots.let { onDataChanged(it) }

        /* Activity listening for navigation requests (onClick item and onClick filters) */
        this.navigationListener = activity as OnNavigateToFragment

        /* This listening for ViewModel requests to update UI */
        this.viewModel.registerListener(this)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {

        /* Unregister listeners */
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

        Log.i(TAG, "onDataReceived() called")

        data?.let{ onDataChanged(it as ArrayList<ParkingLot>) }
    }

    /* Action triggered when ParkingLotsFiltersFragment requests to update UI */
    override fun onFiltersListReceived(filters: ArrayList<Filter>?) {

        Log.i(TAG, "onFiltersListReceived() called")

        activity?.onBackPressed()

        /* Order ViewModel to use logic to apply filters and then request to update UI (triggers onDataReceived) */
        filters?.let {

            this.viewModel.applyFilters(it)

            for(filter in it)
                Log.i(TAG, filter.toString())
        }
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        Log.i(TAG, "onClickEvent() called")

        /* Generate details Fragment */
        val itemDetail = ParkingLotInfoFragment()

        /* Add arguments */
        val args = Bundle()
        data?.let { args.putParcelable(EXTRA_PARKING_LOT, it as ParkingLot) }
        itemDetail.arguments = args

        /* Notify observer to add new Fragment */
        this.navigationListener?.onNavigateToFragment(itemDetail)
    }
}