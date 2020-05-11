package com.example.projetocm_g11.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "parking_lots")
@Parcelize
class ParkingLot(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    val identityID: Int,
    val id: String,
    val latitude: String,
    val longitude: String,
    val active: Int,
    val lastUpdatedAt: Date,
    val type: String,
    val maxCapacity: Int,
    val name: String,
    val occupancy: Int,
    var isFavourite: Boolean = false
) : Parcelable {

    fun getCapacityPercent(): Int {

        if(occupancy == 0) {

            return 0
        }

        return occupancy * 100 / maxCapacity
    }

    fun getTypeEnum(): Type {

        return if(this.type == "Estrutura") {

            Type.UNDERGROUND

        } else Type.SURFACE
    }
}

enum class Type {

    SURFACE, UNDERGROUND
}