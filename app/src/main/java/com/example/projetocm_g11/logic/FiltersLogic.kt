package com.example.projetocm_g11.logic

import com.example.projetocm_g11.domain.data.Filter

class FiltersLogic {

    fun addFilter(filters: ArrayList<Filter>, filter: Filter): ArrayList<Filter> {

        if(!filters.contains(filter)) {

            filters.add(filter)
        }

        return filters
    }

    fun removeFilter(filters: ArrayList<Filter>, filter: Filter): ArrayList<Filter> {

        if(filters.contains(filter)) {

            filters.remove(filter)
        }

        return filters
    }
}