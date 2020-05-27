package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler

class Connectivity(private val context: Context) : Runnable {

    private val TIME_BETWEEN_UPDATES = 5000L

    companion object {

        private var instance: Connectivity? = null
        private val handler = Handler()

        private var listener: OnConnectivityStatusListener? = null

        fun start(context: Context) {

            instance = if(instance == null) {
                Connectivity(
                    context
                )
            } else {
                instance
            }
            instance?.start()
        }

        fun registerListener(listener: OnConnectivityStatusListener) {

            Companion.listener = listener
        }

        fun unregisterListener() {

            listener = null
        }

        fun notifyObservers(connected: Boolean) {

            listener?.onConnectivityStatus(connected)
        }
    }

    private fun start() {

        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }

    private fun getConnectionStatusNow(): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnected == true
    }

    override fun run() {

        val connected = getConnectionStatusNow()

        notifyObservers(
            connected
        )

        handler.postDelayed(this, TIME_BETWEEN_UPDATES)
    }
}