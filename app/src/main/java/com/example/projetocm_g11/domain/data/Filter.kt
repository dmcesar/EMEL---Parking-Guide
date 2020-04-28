package com.example.projetocm_g11.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(val type: FilterType, val value: String = "") : Parcelable { }

enum class FilterType {

    TYPE, AVAILABILITY, FAIR, DISTANCE, ALPHABETICAL
}