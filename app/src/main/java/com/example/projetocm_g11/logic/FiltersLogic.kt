package com.example.projetocm_g11.logic

import com.example.projetocm_g11.domain.Storage
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FiltersLogic {

    private var listener: OnDataReceived? = null

    private val storage = Storage.getInstance()

    fun getAll() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = storage.getFilters()

            notifyDataChanged(list)
        }
    }

    fun addFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.addFilter(filter)

            getAll()
        }
    }

    fun removeFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.removeFilter(filter)

            getAll()
        }
    }

    private suspend fun notifyDataChanged(list: ArrayList<Filter>) {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(list)
        }
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }
}