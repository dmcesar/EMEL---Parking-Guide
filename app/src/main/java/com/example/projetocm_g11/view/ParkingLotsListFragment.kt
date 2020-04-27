package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.projetocm_g11.R
import com.example.projetocm_g11.adapters.ParkingLotLandscapeAdapter
import com.example.projetocm_g11.adapters.ParkingLotPortraitAdapter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*

class ParkingLotsListFragment : Fragment(), OnDataReceived {

    private var listener: OnClickEvent? = null

    fun registerListener(listener: OnClickEvent) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        return view
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        if(parentFragment?.context?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {

            parking_lots.adapter = ParkingLotPortraitAdapter(
                this.listener!!,
                activity as Context,
                R.layout.parking_lots_portrait_list_item,
                list as ArrayList<ParkingLot>
            )

        } else {

            parking_lots.adapter = ParkingLotLandscapeAdapter(
                listener!!,
                activity as Context,
                R.layout.parking_lots_landscape_list_item,
                list as ArrayList<ParkingLot>
            )
        }
    }
}
