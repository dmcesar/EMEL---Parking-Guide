package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter

interface OnFiltersReceivedListener {

    suspend fun getAll(): List<Filter>

    suspend fun insert(filter: Filter)

    suspend fun delete(filter: Filter)
}