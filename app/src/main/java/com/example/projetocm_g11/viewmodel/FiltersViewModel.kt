package com.example.projetocm_g11.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.logic.FiltersLogic

class FiltersViewModel : ViewModel() {

    var filters = ArrayList<Filter>()

    private val logic = FiltersLogic()

    fun toggleFilter(filter: Filter) {

        if(this.filters.contains(filter)) {

            this.filters.remove(filter)

        } else this.filters.add(filter)
    }
}