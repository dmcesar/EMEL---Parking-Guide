package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.adapters.ParkingLotPortraitAdapter

import com.example.projetocm_g11.R
import com.example.projetocm_g11.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.view.ParkingLotsListFragment.ParkingLot"

class ParkingLotsListFragment : Fragment(), OnDataReceived, OnClickEvent {

    private var listener: OnNavigateToFragment? = null

    private lateinit var viewModel: ParkingLotsViewModel

    @OnClick(R.id.button_filter)
    fun showPopupFilter() {

        // Inflate popup view using inflater
        val popupView = layoutInflater.inflate(R.layout.popup_filter, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(popupView, width, height)

        // Close popup when clicked outside
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Finally, show the popup window on top of the vehicles list
        TransitionManager.beginDelayedTransition(parking_lots)
        popupWindow.showAtLocation(parking_lots, Gravity.BOTTOM, 0, 0)
    }

    private fun registerListener() {

        this.listener = activity as OnNavigateToFragment
    }

    private fun unregisterListener() {

        this.listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        ButterKnife.bind(this, view)

        return view
    }

    override fun onStart() {

        registerListener()

        viewModel.registerListener(this)
        super.onStart()
    }

    override fun onStop() {

        unregisterListener()

        viewModel.unregisterListener()
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            parking_lots.adapter = ParkingLotPortraitAdapter(
                this,
                activity as Context,
                R.layout.parking_lots_portrait_list_item,
                list as ArrayList<ParkingLot>
            )

        } else {

            parking_lots.adapter = ParkingLotLandscapeAdapter(
                this,
                activity as Context,
                R.layout.parking_lots_landscape_list_item,
                list as ArrayList<ParkingLot>
            )
        }
    }

    override fun onClickEvent(data: Any) {

        val itemDetail = ParkingLotInfoFragment()

        val args = Bundle()
        args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)
        itemDetail.arguments = args

        this.listener?.onNavigateToFragment(itemDetail)
    }
}
