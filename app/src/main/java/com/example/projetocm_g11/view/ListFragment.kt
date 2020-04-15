package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetocm_g11.OnClickEvent
import com.example.projetocm_g11.OnDataReceived
import com.example.projetocm_g11.ParkingLotAdapter

import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.parking_lots

class ListFragment : Fragment(), OnDataReceived, OnClickEvent {

    private var listener: OnClickEvent? = null

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        view.parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        return view
    }

    override fun onStart() {

        viewModel.registerListener(this)
        registerListener(activity as OnClickEvent)
        super.onStart()
    }

    override fun onStop() {

        viewModel.unregisterListener()
        unregisterListener()
        super.onStop()
    }

    override fun onDataReceived(list: ArrayList<ParkingLot>) {

        parking_lots.adapter = ParkingLotAdapter(this, activity as Context, R.layout.item_list, list)
    }

    private fun registerListener(listener: OnClickEvent) {

        this.listener = listener
    }

    private fun unregisterListener() {

        this.listener = null
    }

    override fun onClickEvent() {
        listener?.onClickEvent()
    }
}
