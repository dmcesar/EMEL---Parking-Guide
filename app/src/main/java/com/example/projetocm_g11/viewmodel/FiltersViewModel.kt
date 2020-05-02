package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.FiltersLogic

class FiltersViewModel : ViewModel(), OnDataReceived {

    private var listener: OnDataReceived? = null

    private val logic = FiltersLogic()

    var filters = ArrayList<Filter>()

    fun getAll() {

        this.logic.getAll()
    }

    fun addFilter(filter: Filter) {

        this.logic.addFilter(filter)
    }

    fun removeFilter(filter: Filter) {

        this.logic.removeFilter(filter)
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    private fun notifyDataChanged(list: ArrayList<Filter>) {

        this.listener?.onDataReceived(list)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let {

            notifyDataChanged(data as ArrayList<Filter>)
        }
    }
}