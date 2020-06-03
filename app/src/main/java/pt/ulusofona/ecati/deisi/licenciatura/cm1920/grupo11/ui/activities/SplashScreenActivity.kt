package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationResult
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.SplashViewModel

const val EXTRA_DATA = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.DATA"
const val EXTRA_DATA_FROM_REMOTE = "com.example.projectocm_g11.ui.activities.SplashScreenActivity.DATA_FROM_REMOTE"
const val PREFERENCE_SWITCH_THEMES_NOTIFY = "pt.ulusofona.ecati.SplashScreenActivity.NOTIFY"

const val LOCATION_REQUEST_CODE = 100

class SplashScreenActivity : PermissionsActivity(LOCATION_REQUEST_CODE),
    OnDataReceivedWithOriginListener, OnLocationChangedListener {

    private lateinit var viewModel: SplashViewModel

    private val splashTimeOut: Long = 3000

    private var data: ArrayList<ParkingLot>? = null
    private var updated: Boolean? = null

    /* Flag raised when splash screen minimum time is over and data has not been received */
    private var launchActivityFlag: Boolean = false

    /* Flag raised when data is requested (when location permissions are granted) */
    private var requestDataFlag: Boolean = false

    private fun launchActivity(data: ArrayList<ParkingLot>, updated: Boolean) {

        val intent = Intent(this, MainActivity::class.java)

        intent.putParcelableArrayListExtra(EXTRA_DATA, data)
        intent.putExtra(EXTRA_DATA_FROM_REMOTE, updated)

        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsSuccess() {

        /* Register listener for view model (receive data) */
        this.viewModel.registerListener(this)

        /* Register listener for FusedLocation (receive location updates) */
        FusedLocation.registerActivityListener(this)

        /* When location is received, request parking lots data */
        this.requestDataFlag = true
    }

    override fun onRequestPermissionsFailure() {

        /* If no permissions were given, finish application */
        this.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        this.viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        Handler().postDelayed({

            if(this.updated != null && this.data != null) {

                launchActivity(this.data!!, this.updated!!)

            } else {

                launchActivityFlag = true
            }

        }, splashTimeOut)
    }

    override fun onStart() {

        /* Request location permission */
        super.onRequestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))

        super.onStart()
    }

    override fun onStop() {

        /* Unregister listeners */
        this.viewModel.unregisterListener()
        FusedLocation.unregisterActivityListener()

        super.onStop()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        if(launchActivityFlag) {

            launchActivity(data, updated)

        } else {

            this.data = data
            this.updated = updated
        }
    }

    override fun onLocationChanged(locationResult: LocationResult) {

        if(this.requestDataFlag) {

            this.requestDataFlag = false
            this.viewModel.requestData(locationResult.lastLocation)
        }
    }
}
