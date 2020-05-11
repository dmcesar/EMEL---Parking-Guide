package com.example.projetocm_g11.data.local.list

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.widget.FitWindowsFrameLayout
import com.example.projetocm_g11.data.local.entities.Filter
import com.google.gson.GsonBuilder

const val FILTERS_PREFERENCES = "com.example.projetocm_g11.domain.filters.FiltersLogic.FILTERS_LIST"

class Storage {

    private val storage = ArrayList<Filter>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if (instance == null) {

                    instance = Storage()
                }

                return instance as Storage
            }
        }
    }

    fun getAll(): ArrayList<Filter> {

        return storage
    }

    fun insert(filter: Filter) {

        storage.add(filter)
    }

    fun delete(filter: Filter) {

        storage.remove(filter)
    }
}