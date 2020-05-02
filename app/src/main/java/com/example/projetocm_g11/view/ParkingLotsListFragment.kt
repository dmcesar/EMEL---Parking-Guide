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

        /* Set init value for list */
        this.viewModel.parkingLots.let { onDataChanged(ArrayList(it)) }

        /* Activity listening for navigation requests (onClick item and onClick filters) */
        this.navigationListener = activity as OnNavigateToFragment

        /* This listening for ViewModel requests to update UI */
        this.viewModel.registerListener(this)

        /* Get parking lots from storage and update UI */
        this.viewModel.getAll()

        super.onStart()
    }

    override fun onDestroy() {

        this.navigationListener = null

        this.viewModel.unregisterListener()

        super.onDestroy()
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

    override fun onSwipeEvent(data: Any?, direction: Int) {

        /* Swiped right: Show map */
        if(direction == 1) {

            val args = Bundle()

            args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)

            val navigationFragment = NavigationFragment()
            navigationFragment.arguments = args

            this.navigationListener?.onNavigateToFragment(navigationFragment)
        }

        /* Swiped left: Tag/Untag as favorite */
        else {

            this.viewModel.toggleFavorite(data as String)
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