package com.example.projetocm_g11.domain.data

import java.util.*

class ParkingLot(
    val id: String,
    val name: String,
    val active: Boolean,
    val identityID: Int,
    val maxCapacity: Int,
    occupancy: Int,
    var lastUpdatedAt: Date,
    val latitude: Double,
    val longitude: Double,
    val type: Type
) {

    val capacityPercent: Int = occupancy

        // Returns capacity in %
        get() {

           return field * 100 / maxCapacity
        }

    /* Returns state in String format according to capacity */
    fun getState(): String {

        if(capacityPercent == 100) { return "Full"}

        return if(capacityPercent >= 90) "Potentially full"  else "Free"
    }
}

enum class Type {

    SURFACE, UNDERGROUND
}