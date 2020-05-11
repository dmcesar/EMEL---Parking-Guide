package com.example.projetocm_g11.data.local.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    val value: String
) : Parcelable