package pt.ulusofona.ecati.deisi.ui.utils

import android.app.Application
import pt.ulusofona.ecati.deisi.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.data.sensors.location.FusedLocation

class ParkingLotsApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        /* Start reading sensors and notify observers */
        FusedLocation.start(this)
        Battery.start(this)
    }
}