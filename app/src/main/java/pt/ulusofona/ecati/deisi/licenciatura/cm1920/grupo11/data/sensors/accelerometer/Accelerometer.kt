package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class Accelerometer private constructor(val context: Context) : SensorEventListener {

    private val TAG = Accelerometer::class.java.simpleName

    private var lastUpdate: Long = 0

    private lateinit var sensorManager: SensorManager

    companion object {

        private var instance: Accelerometer? = null

        private var filtersListener: OnAccelerometerEventListener? = null
        private var parkingLotsListener: OnAccelerometerEventListener? = null

        fun start(context: Context) {

            instance = if(instance == null) {
                Accelerometer(
                    context
                )
            } else {
                instance
            }
            instance?.start()
        }

        fun registerFiltersListener(listener: OnAccelerometerEventListener) {

            this.filtersListener = listener
        }

        fun unregisterFiltersListener() {

            this.filtersListener = null
        }

        fun registerParkingLotsListener(listener: OnAccelerometerEventListener) {

            this.parkingLotsListener = listener
        }

        fun unregisterParkingLotsListener() {

            this.parkingLotsListener = null
        }

        fun notifyObservers() {

            this.filtersListener?.onAccelerometerEventListener()
            this.parkingLotsListener?.onAccelerometerEventListener()
        }
    }

    private fun start() {

        this.sensorManager = this.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        this.lastUpdate = System.currentTimeMillis()
    }

    private fun readAccelerometer(event: SensorEvent) {

        val values = event.values

        /* Axis movement */
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accelerationSquareRoot = ((x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH))

        val actualTime = event.timestamp

        if(accelerationSquareRoot >= 2) {

            if(actualTime - lastUpdate < 200) {

                return
            }

            Log.i(TAG, "Device moved")
            notifyObservers()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

            readAccelerometer(event)
        }
    }
}