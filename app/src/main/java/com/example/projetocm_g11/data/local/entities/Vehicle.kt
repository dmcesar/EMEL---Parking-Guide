package com.example.projetocm_g11.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "vehicles")
@Parcelize
data class Vehicle(
    @PrimaryKey val uuid: String = UUID.randomUUID().toString(),
    var brand: String,
    var model: String,
    var plate: String,
    var plateDate: Date = Date()
) : Parcelable{

    fun setDate(date: Date) {
        plateDate = date
    }

    fun getDate(): String {

        val sdf = SimpleDateFormat("yy-MM")

        return sdf.format(plateDate)
    }
}