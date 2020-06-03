package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.accelerometer.Accelerometer
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity.Connectivity
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation

const val CHANNEL_ID = "pt.ulusofona.ecati.ParkingLotsListFragment.NOTIFICATION_CHANNEL"

class ParkingLotsApplication : Application() {

    private fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {

        super.onCreate()

        createNotificationChannel()

        /* Start reading sensors and notify observers */
        FusedLocation.start(this)
        Battery.start(this)
        Connectivity.start(this)
        Accelerometer.start(this)
    }
}