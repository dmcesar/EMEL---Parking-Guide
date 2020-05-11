package com.example.projetocm_g11.ui.listeners

import com.example.projetocm_g11.data.local.entities.Filter

interface OnFiltersEvent {

    suspend fun getAll(): List<Filter>

    suspend fun insert(filter: Filter)

    suspend fun delete(filter: Filter)
}