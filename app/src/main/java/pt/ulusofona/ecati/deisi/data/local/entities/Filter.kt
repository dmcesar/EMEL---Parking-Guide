package pt.ulusofona.ecati.deisi.data.local.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    val value: String
) : Parcelable