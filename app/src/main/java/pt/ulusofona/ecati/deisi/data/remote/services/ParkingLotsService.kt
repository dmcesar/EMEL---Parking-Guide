package pt.ulusofona.ecati.deisi.data.remote.services

import pt.ulusofona.ecati.deisi.data.remote.responses.ParkingLotsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ParkingLotsService {

    @GET("parking/lots")
    suspend fun getAll(@Header("api_key") token: String)
            : Response<List<ParkingLotsResponse>>

}