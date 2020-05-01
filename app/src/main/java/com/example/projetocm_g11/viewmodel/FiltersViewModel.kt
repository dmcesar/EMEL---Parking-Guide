package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.Logic
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.interfaces.OnDataReceived

class FiltersViewModel : ViewModel(), OnDataReceived {

    private val logic = Logic.getInstance()

    private var listener: OnDataReceived? = null

    /* Observable objects */
    var filters = ArrayList<Filter>()

    fun getFilters() {

        this.logic.getFilters()
    }

    fun applyFilters() {

        this.logic.applyFilters()
    }

    fun addFilter(filter: Filter) {

        this.logic.addFilter(filter)
    }

    fun removeFilter(filter: Filter) {

        this.logic.removeFilter(filter)
    }

    fun registerListeners(listener: OnDataReceived) {

        this.listener = listener
        this.logic.registerFiltersListener(this)
    }

    fun unregisterListeners() {

        this.listener = null
        this.logic.unregisterFiltersListener()
    }

    /* Notify observer of change in data */
    private fun notifyDataChanged() {

        this.listener?.onDataReceived(this.filters)
    }

    /* Method called by observable (logic) */
    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.filters = it as ArrayList<Filter> }

        notifyDataChanged()
    }
}