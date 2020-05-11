package com.example.projetocm_g11.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.util.*

data class ParkingLotsResponse(
    @SerializedName("id_entidade") val identityID: Int,
    @SerializedName("id_parque") val id: String,
    val latitude: String,
    val longitude: String,
    @SerializedName("activo") val active: Int,
    @SerializedName("data_ocupacao") val lastUpdatedAt: Date,
    @SerializedName("tipo") val type: String,
    @SerializedName("capacidade_max") val maxCapacity: Int,
    @SerializedName("nome") val name: String,
    @SerializedName("ocupacao") val occupancy: Int
)