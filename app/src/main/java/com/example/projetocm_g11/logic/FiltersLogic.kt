package com.example.projetocm_g11.logic

import android.util.Log
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FiltersLogic {

    private val TAG = FiltersLogic::class.java.simpleName

    private var listener: OnDataReceived? = null

    private var filters = ArrayList<Filter>()

    fun update(list: ArrayList<Filter>) {

        CoroutineScope(Dispatchers.IO).launch {

            filters = list

            Log.i(TAG, "Updated list")

            notifyDataChanged()
        }
    }

    fun addFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            if(!filters.contains(filter)) {

                filters.add(filter)

                Log.i(TAG, "Added $filter")

                notifyDataChanged()
            }
        }
    }

    fun removeFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            if(filters.contains(filter)) {

                filters.remove(filter)

                Log.i(TAG, "Removed $filter")

                notifyDataChanged()
            }
        }
    }

    fun registerListener(listener: OnDataReceived) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    private suspend fun notifyDataChanged() {

        withContext(Dispatchers.Main) {

            listener?.onDataReceived(filters)
        }
    }
}