package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.logic.FiltersLogic

class FiltersViewModel : ViewModel() {

    var filters = ArrayList<Filter>()

    private val logic = FiltersLogic()

    fun addFilter(filter: Filter) {

        this.filters = this.logic.addFilter(filters, filter)
    }

    fun removeFilter(filter: Filter) {

        this.filters = this.logic.removeFilter(filters, filter)
    }
}