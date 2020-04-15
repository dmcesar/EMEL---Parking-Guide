package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.OnDataReceived
import com.example.projetocm_g11.domain.ListLogic
import com.example.projetocm_g11.domain.data.ParkingLot

class ListViewModel : ViewModel(), OnDataReceived {

    private var listener: OnDataReceived? = null
    private val listLogic = ListLogic()

    private var list = ArrayList<ParkingLot>()

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.listLogic.registerListener(this)

        this.listLogic.fetchAll()
    }

    fun unregisterListener() {

        this.listener = null
        this.listLogic.unregisterListener()
    }

    private fun notifyListChanged() {

        this.listener?.onDataReceived(list)
    }

    override fun onDataReceived(list: ArrayList<ParkingLot>) {

        this.list = list
        notifyListChanged()
    }
}