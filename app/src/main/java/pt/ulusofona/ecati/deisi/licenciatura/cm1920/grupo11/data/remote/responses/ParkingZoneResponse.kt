package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ParkingZoneResponse(

    val id: Int,
    @SerializedName("produto") val product: String,
    @SerializedName("cod_tarifa") val tariffCode: String,
    @SerializedName("tarifa") val tariff: String,
    @SerializedName("cod_horario") val scheduleCode: String,
    @SerializedName("horario") val schedule: String,
    @SerializedName("id_tipo_estacionamento") val typeID: String,
    @SerializedName("tipo_estacionamento") val type: String,
    @SerializedName("observacoes") val observations: String
)