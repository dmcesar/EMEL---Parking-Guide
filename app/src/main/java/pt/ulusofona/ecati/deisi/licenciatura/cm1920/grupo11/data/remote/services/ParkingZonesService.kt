package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.services

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.requests.ParkingZoneRequest
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface ParkingZonesService {

    @GET("/parking/zone/")
    suspend fun getZone(@Header("api_key") token: String, @Body body: ParkingZoneRequest)
            : Response<ParkingZoneResponse>
}