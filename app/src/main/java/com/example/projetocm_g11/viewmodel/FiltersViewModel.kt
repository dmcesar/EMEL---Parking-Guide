package com.example.projetocm_g11.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.logic.FiltersLogic

class FiltersViewModel : ViewModel(), OnDataReceived {

    private val TAG = FiltersViewModel::class.java.simpleName

    private var listener: OnDataReceived? = null

    var filters = ArrayList<Filter>()

    private val logic = FiltersLogic()

    fun update(list: ArrayList<Filter>) {

        this.logic.update(list)
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

    private fun notifyDataChanged() {

        Log.i(TAG, this.filters.size.toString())

        this.listener?.onDataReceived(this.filters)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { this.filters = it as ArrayList<Filter> }

        notifyDataChanged()
    }
}