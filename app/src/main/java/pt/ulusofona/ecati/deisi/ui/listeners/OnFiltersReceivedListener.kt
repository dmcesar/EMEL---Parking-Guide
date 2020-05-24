package pt.ulusofona.ecati.deisi.ui.listeners

import pt.ulusofona.ecati.deisi.data.local.entities.Filter

interface OnFiltersReceivedListener {

    suspend fun getAll(): List<Filter>

    suspend fun insert(filter: Filter)

    suspend fun delete(filter: Filter)
}