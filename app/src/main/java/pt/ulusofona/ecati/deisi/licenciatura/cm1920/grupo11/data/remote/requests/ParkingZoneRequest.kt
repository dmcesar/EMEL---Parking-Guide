package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.remote.requests

import retrofit2.http.Header

data class ParkingZoneRequest(@Header("api_key") val token: String, val latitude: Double, val longitude: Double)