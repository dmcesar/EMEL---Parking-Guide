package com.example.projetocm_g11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.projetocm_g11.data.local.entities.Filter
import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener
import com.example.projetocm_g11.domain.filters.FiltersLogic

class FiltersViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    /* Creates domain component */
    private val logic = FiltersLogic()

    /* Listener is notified with ArrayList<Filter> */
    private var listener: OnDataReceivedListener? = null

    /* Fragment lifecycle variable */
    var filters = ArrayList<Filter>()

    fun read() {

        this.logic.read()
    }

    fun insert(filter: Filter) {

        this.logic.insert(filter)
    }

    fun delete(filter: Filter) {

        this.logic.delete(filter)
    }

    fun registerListener(listener: OnDataReceivedListener) {

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

            filters = it as ArrayList<Filter>

            notifyDataChanged(filters)
        }
    }
}