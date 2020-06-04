package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ParkingZoneResponse(

    private val id: Int,
    @SerializedName("produto") private val product: String,
    @SerializedName("cod_tarifa") private val tariffCode: String,
    @SerializedName("tarifa") val tariff: String,
    @SerializedName("cod_horario") private val scheduleCode: String,
    @SerializedName("horario") val schedule: String,
    @SerializedName("id_tipo_estacionamento") private val typeID: String,
    @SerializedName("tipo_estacionamento") val type: String,
    @SerializedName("observacoes") val observations: String
)