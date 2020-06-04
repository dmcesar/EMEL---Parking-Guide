package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.services

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.requests.ParkingZoneRequest
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.responses.ParkingZoneResponse
import retrofit2.Response
import retrofit2.http.GET

interface ParkingZonesService {

    @GET("/parking/zone/")
    suspend fun getAll(request: ParkingZoneRequest)
            : Response<ParkingZoneResponse>
}