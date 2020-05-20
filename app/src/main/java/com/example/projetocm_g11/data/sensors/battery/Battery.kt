package com.example.projetocm_g11.data.sensors.battery

import android.content.Context
import android.os.BatteryManager
import android.os.Handler

class Battery private constructor(private val context: Context) : Runnable {

    private val TAG = Battery::class.java.simpleName

    private val TIME_BETWEEN_UPDATES = 5000L

    companion object {

        private var instance: Battery? = null
        private val handler = Handler()

        private var listener: OnBatteryCapacityListener? = null

        fun start(context: Context) {

            instance = if(instance == null) { Battery(context) } else { instance }
            instance?.start()
        }

        fun registerListener(listener: OnBatteryCapacityListener) {

            this.listener = listener
        }

        fun unregisterListener() {

            this.listener = null
        }

        fun notifyObservers(batteryCapacity: Int) {

            this.listener?.onBatteryCapacityListener(batteryCapacity)
        }
    }

    private fun start() {

        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }

    private fun getBatteryCapacityNow(): Int {

        val manager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        return manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    override fun run() {

        val capacity = getBatteryCapacityNow()

        notifyObservers(capacity)

        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }
}