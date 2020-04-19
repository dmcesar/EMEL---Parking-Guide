package com.example.projetocm_g11.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class Vehicle(var brand: String, var model: String, var plate: String, private var plateDate: Date = Date()) : Parcelable{

    @IgnoredOnParcel
    val uuid = UUID.randomUUID().toString()

    fun setDate(date: Date) {
        plateDate = date
    }

    fun getDate(): String {

        val sdf = SimpleDateFormat("yy-MM")

        return sdf.format(plateDate)
    }
}