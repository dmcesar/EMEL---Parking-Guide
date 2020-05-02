package com.example.projetocm_g11.interfaces

import com.example.projetocm_g11.domain.data.Filter

interface OnFiltersListReceived {

    fun onFiltersListReceived(filters: ArrayList<Filter>?)
}