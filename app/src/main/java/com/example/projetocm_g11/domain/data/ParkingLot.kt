package com.example.projetocm_g11.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class ParkingLot(
    val id: String,
    val name: String,
    val active: Boolean,
    val identityID: Int,
    val maxCapacity: Int,
    val occupancy: Int,
    val lastUpdatedAt: Date,
    val latitude: Double,
    val longitude: Double,
    val type: Type,
    var isFavourite: Boolean = false
) : Parcelable {

    fun getCapacityPercent(): Int {

        return occupancy * 100 / maxCapacity
    }
}

enum class Type {

    SURFACE, UNDERGROUND
}