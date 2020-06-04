package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.services

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse
import retrofit2.Response
import retrofit2.http.*

interface ParkingZonesService {

    @GET("parking/zone/")
    suspend fun getZone(
        @Header("api_key") token: String,
        @Query(value = "latitude") latitude: Double,
        @Query(value = "longitude") longitude: Double
    )
            : Response<ParkingZoneResponse>
}