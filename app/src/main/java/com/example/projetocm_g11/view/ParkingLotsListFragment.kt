package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.adapters.ParkingLotAdapter

import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Type
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*
import java.util.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.view.ParkingLotsListFragment.ParkingLot"

class ParkingLotsListFragment : Fragment(), OnDataReceived, OnClickEvent {

    private var listener: OnNavigateToFragment? = null

    private lateinit var viewModel: ParkingLotsViewModel

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

        return view
    }

    override fun onStart() {

        val list = ArrayList<ParkingLot>()

        val p1 = ParkingLot("P019", "El Corte Inglés", true, 4, 800, 800, Date(), 38.75463622, -9.3414141, Type.SURFACE)

        list.add(p1)
        parking_lots.adapter = ParkingLotAdapter(this, activity as Context, R.layout.parking_lots_list_item, list)

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

        parking_lots.adapter = ParkingLotAdapter(
            this,
            activity as Context,
            R.layout.parking_lots_list_item,
            list as ArrayList<ParkingLot>)
    }

    override fun onClickEvent(data: Any) {

        val itemDetail = ParkingLotInfoFragment()

        val args = Bundle()
        args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)
        itemDetail.arguments = args

        this.listener?.onNavigateToFragment(itemDetail)
    }
}
