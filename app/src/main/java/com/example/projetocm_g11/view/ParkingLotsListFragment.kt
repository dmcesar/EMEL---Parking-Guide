package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
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
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import kotlinx.android.synthetic.main.fragment_parking_lots_list.view.*

class ParkingLotsListFragment : Fragment(), OnDataReceived {

    private lateinit var viewModel: ParkingLotsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_list, container, false)

        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        return view
    }

    override fun onStart() {

        val list = ArrayList<ParkingLot>()

        val p1 = ParkingLot(Type.UNDERGROUND, "Parque de lisboa", "Lisboa")

        list.add(p1)
        parking_lots.adapter = ParkingLotAdapter(activity as Context, R.layout.parking_lots_list_item, list)

        viewModel.registerListener(this)
        super.onStart()
    }

    override fun onStop() {

        viewModel.unregisterListener()
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        parking_lots.adapter = ParkingLotAdapter(
            activity as Context,
            R.layout.parking_lots_list_item,
            list as ArrayList<ParkingLot>)
    }
}
