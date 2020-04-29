package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.ParkingLotsLogic

class ParkingLotsViewModel : ViewModel(), OnDataReceived {

    private val logic = ParkingLotsLogic()

    private var listener: OnDataReceived? = null

    /* Observable object */
    var parkingLots = mutableListOf<ParkingLot>()
    var filters = ArrayList<Filter>()

    private fun fetchList() {

        this.logic.getList()
    }

    fun applyFilters(list: ArrayList<Filter>) {

        this.filters = list
        this.logic.applyFilters(filters)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    private fun notifyDataChanged() {

        this.listener?.onDataReceived(ArrayList(this.parkingLots))
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.parkingLots = it as ArrayList<ParkingLot> }

        notifyDataChanged()
    }

    init {

        fetchList()
    }
}