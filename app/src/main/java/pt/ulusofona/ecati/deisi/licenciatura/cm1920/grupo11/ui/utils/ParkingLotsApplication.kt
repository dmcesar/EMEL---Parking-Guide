package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils

import android.app.Application
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.accelerometer.Accelerometer
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.Connectivity
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation

class ParkingLotsApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        /* Start reading sensors and notify observers */
        FusedLocation.start(this)
        Battery.start(this)
        Connectivity.start(this)
        Accelerometer.start(this)
    }
}