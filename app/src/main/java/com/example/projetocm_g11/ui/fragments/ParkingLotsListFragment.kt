package com.example.projetocm_g11.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.ui.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.ui.adapters.ParkingLotPortraitAdapter
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.ui.activities.EXTRA_DATA
import com.example.projetocm_g11.ui.activities.EXTRA_DATA_FROM_REMOTE
import com.example.projetocm_g11.ui.listeners.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.ui.fragments.ParkingLotsListFragment.ParkingLot"

class ParkingLotsListFragment : Fragment(), OnTouchListener {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var navigationListener: OnNavigationListener? = null

    private var data: ArrayList<ParkingLot>? = null
    private var dataIsFromRemote: Boolean? = null
    private var dataFetchedBefore: Boolean? = null

    @OnClick(R.id.button_filter)
    fun onClickGoFiltersFragment() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.arguments?.let {

            this.data = it.getParcelableArrayList(EXTRA_DATA)
            this.dataIsFromRemote = it.getBoolean(EXTRA_DATA_FROM_REMOTE)
            this.dataFetchedBefore = it.getBoolean(EXTRA_DATA_FETCHED_BEFORE)

            Log.i(TAG, "${(this.dataIsFromRemote).toString()}  ${(this.dataFetchedBefore).toString()}")
        }

        this.arguments = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /* Inflate layout */
        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        /* Bind fragment to view */
        ButterKnife.bind(this, view)

        return view
    }

    override fun onStart() {

        this.navigationListener = parentFragment as OnNavigationListener

        this.data?.let { initAdapter(it) }

        this.dataFetchedBefore?.let { fetchedBefore ->

            if(fetchedBefore) {

                this.dataIsFromRemote?.let { fromRemote ->

                    if(!fromRemote) {

                        AlertDialog.Builder(activity as Context)
                            .setTitle(R.string.outdatedDataTitle)
                            .setMessage(R.string.outdatedDataMessage)
                            .setNegativeButton(R.string.OK, null)
                            .show()
                    }
                }
            }
        }

        super.onStart()
    }

    override fun onStop() {

        this.navigationListener = null

        super.onStop()
    }

    /* Updates RecycleView based on received data and screen orientation */
    private fun initAdapter(data: ArrayList<ParkingLot>) {

        parking_lots.layoutManager = LinearLayoutManager(activity as Context)

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

    override fun onSwipeEvent(data: Any?, direction: Int) {

        /* Swiped right: Show map */
        if(direction == 1) {

            Log.i(TAG, "Item swiped right")

            /* Create arguments with parking lot */
            val args = Bundle()
            args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)

            /* Notify observer to navigate to ParkingLotNavigationFragment with created args */
            this.navigationListener?.onNavigateToParkingLotNavigation(args)
        }

        /* Swiped left: Tag/Untag as favorite */
        else {

            Log.i(TAG, "Item swiped left")

        }
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        Log.i(TAG, "Item clicked")

        /* Create arguments with parking lot */
        val args = Bundle()
        args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)

        /* Notify observer to navigate to ParkingLotDetailsFragment with created args */
        this.navigationListener?.onNavigateToParkingLotDetails(args)
    }
}