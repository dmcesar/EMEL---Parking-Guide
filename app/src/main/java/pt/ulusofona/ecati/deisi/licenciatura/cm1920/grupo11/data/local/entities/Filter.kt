package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    val value: String
) : Parcelable