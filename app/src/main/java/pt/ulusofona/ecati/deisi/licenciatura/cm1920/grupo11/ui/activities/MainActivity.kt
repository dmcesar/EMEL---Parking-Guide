package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.location.LocationResult
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.NavigationManager
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.battery.Battery
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.battery.OnBatteryCapacityListener
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.FusedLocation
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.sensors.location.OnLocationChangedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnNavigationListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments.PREFERENCE_THEMES
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val PREFERENCE_APPLIED_THEME = "com.example.projetocm_g11.ui.activities.APPLIED_THEME"
const val PREFERENCE_QUEUED_THEME = "com.example.projetocm_g11.ui.activities.QUEUED_THEME"

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnNavigationListener,
    OnDataReceivedListener,
    OnBatteryCapacityListener,
    OnLocationChangedListener {

    private lateinit var viewModel: MainViewModel

    /* Received user location (null if no location permissions are granted) */
    private var userLocation: Location? = null

    /* Flag activated when closest parking lot is requested */
    private var requestedClosestParkingLot = false

    @OnClick(R.id.button_park_me_now)
    fun navigateToClosestParkingLot() {

        this.userLocation?.let {

            this.viewModel.getClosestParkingLotCoordinates(it)

        } ?: kotlin.run {

            this.requestedClosestParkingLot = true
        }
    }

    private fun initFragment() {

        /* Read arguments from SplashScreenActivity */
        val data: ArrayList<ParkingLot>? = intent?.getParcelableArrayListExtra(
            EXTRA_DATA
        )
        val updated = intent?.getBooleanExtra(EXTRA_DATA_FROM_REMOTE, false)

        /* Create arguments */
        val args = Bundle()
        args.putParcelableArrayList(EXTRA_DATA, data)
        args.putBoolean(EXTRA_DATA_FROM_REMOTE, updated!!)

        // Navigate to list fragment
        NavigationManager.goToParkingLotsFragment(supportFragmentManager, args)
    }

    /* Called when navigation occurs.
    * Validates which theme should be applied, according to current time.
    * If a switch should occur, queue theme ID and recreate activity to apply it. */
    @SuppressLint("SimpleDateFormat")
    private fun validateThemeTime() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        /* Check if switch themes automatically is ON (default value is ON) */
        val switchThemes = sharedPreferences.getBoolean(PREFERENCE_THEMES, true)

        /* If the app should not change themes, return */
        if(!switchThemes) {

            return
        }

        /* Get currently applied theme ID (default theme is LightTheme) */
        val currentTheme = sharedPreferences.getInt(PREFERENCE_APPLIED_THEME, R.style.LightTheme)

        /* Date formatter */
        val dateFormatter = SimpleDateFormat("HH:mm")

        /* Date timestamps */
        val morningDate = dateFormatter.parse("08:00")
        val afternoonDate = dateFormatter.parse("16:00")

        /* Check what time it is */
        val currentHour = dateFormatter.parse(dateFormatter.format(Date()))

        /* If current theme is LightTheme*/
        if(currentTheme == R.style.LightTheme) {

            /* Check if should be in DarkTheme */
            if(currentHour.before(morningDate) || currentHour.after(afternoonDate)) {

                /* Queue DarkTheme */
                sharedPreferences.edit().putInt(PREFERENCE_QUEUED_THEME, R.style.DarkTheme).apply()
            }
        }

        /* If current theme is DarkTheme */
        else if(currentTheme == R.style.DarkTheme) {

            /* Check if should be in LightTheme */
            if(currentHour.after(morningDate) && currentHour.before(afternoonDate)) {

                /* Queue LightTheme */
                sharedPreferences.edit().putInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme).apply()
            }
        }
    }

    /* Notifies user when battery is low.
    * When positive button is pressed, queues DarkTheme and stops validating themes according to time.
    * When negative button is pressed, does not notify user again until battery surpasses 20%. */
    private fun validateThemeBattery(capacity: Int) {

        CoroutineScope(Dispatchers.Default).launch {

            val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

            val currentTheme = sharedPreferences.getInt(PREFERENCE_APPLIED_THEME, R.style.LightTheme)

            /* If current theme is DarkTheme, cancel(exit) coroutine */
            if(currentTheme == R.style.DarkTheme) {

                return@launch
            }

            val notifyBatteryLow =
                sharedPreferences.getBoolean(PREFERENCE_SWITCH_THEMES_NOTIFY, true)

            /* Check if battery has been recharged */
            if (capacity > 20) {

                sharedPreferences.edit()
                    .putBoolean(PREFERENCE_SWITCH_THEMES_NOTIFY, true)
                    .apply()
            }

            if (notifyBatteryLow && capacity <= 20) {

                /* Cannot notify again until battery is recharged */
                sharedPreferences.edit().putBoolean(PREFERENCE_SWITCH_THEMES_NOTIFY, false).apply()

                withContext(Dispatchers.Main) {

                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.battery_life_low)
                        .setMessage(R.string.switch_to_dark_mode)
                        .setPositiveButton(
                            R.string.OK
                        ) { _, _ ->
                            run {

                                /* Queue Dark theme to apply when navigation occurs.
                                * Stop validating themes according to time until setting is reset. */
                                sharedPreferences.edit()
                                    .putInt(PREFERENCE_QUEUED_THEME, R.style.DarkTheme)
                                    .putBoolean(PREFERENCE_THEMES, false)
                                    .apply()
                            }
                        }
                        .setNegativeButton(R.string.NO, null)
                        .show()
                }
            }
        }
    }

    private fun validateThemes() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val appliedTheme = sharedPreferences.getInt(PREFERENCE_APPLIED_THEME, R.style.LightTheme)
        val queuedTheme = sharedPreferences.getInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme)

        /* If a different theme has been queued (either by time or battery) */
        if(appliedTheme != queuedTheme) {

            /* Recreate activity to view changes */
            recreate()
        }
    }

    private fun updateTheme() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val queuedTheme = sharedPreferences.getInt(PREFERENCE_QUEUED_THEME, R.style.LightTheme)

        sharedPreferences.edit().putInt(PREFERENCE_APPLIED_THEME, queuedTheme).apply()

        setTheme(queuedTheme)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        /* Navigate to selected fragment and checks if themes should change */
        when(item.itemId) {

            R.id.nav_parkings_lots -> {

                NavigationManager.goToParkingLotsFragment(supportFragmentManager, null)

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_vehicles -> {

                NavigationManager.goToVehiclesListFragment(supportFragmentManager)

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_contacts -> {

                NavigationManager.goToContactsFragment(supportFragmentManager)

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_settings -> {

                NavigationManager.goToSettingsFragment(supportFragmentManager)

                validateThemeTime()
                validateThemes()
            }
        }

        /* Close drawer */
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {

        when {
            /* If drawer is open, close drawer */
            drawer.isDrawerOpen(GravityCompat.START) ->
                drawer.closeDrawer(GravityCompat.START)

            /* If there is only one Fragment on the stack, finish */
            supportFragmentManager.backStackEntryCount == 1 ->
                finish()

            else -> {

                validateThemeTime()
                validateThemes()

                super.onBackPressed()
            }
        }
    }

    private fun setupDrawerMenu() {

        // Button to open/close drawer
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)

        // Subscribe activity to interface (navigates to fragment)
        nav_drawer.setNavigationItemSelectedListener(this)

        // Subscribe toggle button to drawer interface (toggles state)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        updateTheme()

        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        this.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Provides drawer access within toolbar
        setSupportActionBar(toolbar)

        // Removes title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        if(!screenRotated(savedInstanceState)) {

            /* Creates and starts ParkingLotsListFragment with previously fetched data */
            initFragment()
        }
    }

    override fun onStart() {

        /* Register listeners  */
        Battery.registerListener(this)
        FusedLocation.registerActivityListener(this)
        this.viewModel.registerListener(this)

        super.onStart()
    }

    override fun onStop() {

        /* Unregister listeners */
        Battery.unregisterListener()
        FusedLocation.unregisterActivityListener()
        this.viewModel.unregisterListener()

        super.onStop()
    }

    /* Sensors listeners */
    override fun onBatteryCapacityListener(capacity: Int) {

        validateThemeBattery(capacity)
    }

    override fun onLocationChanged(locationResult: LocationResult) {

        this.userLocation = locationResult.lastLocation

        if(this.requestedClosestParkingLot) {

            this.requestedClosestParkingLot = false

            this.viewModel.getClosestParkingLotCoordinates(locationResult.lastLocation)
        }
    }

    /* Data/other events listeners*/
    override fun onNavigateToParkingLotDetails(args: Bundle) {

        NavigationManager.goToParkingLotDetailsFragment(supportFragmentManager, args)
    }

    override fun onNavigateToFiltersFragment() {

        validateThemeTime()
        validateThemes()

        NavigationManager.goToFiltersFragment(supportFragmentManager)
    }

    override fun onNavigateToVehicleForm(args: Bundle?) {

        validateThemeTime()
        validateThemes()

        NavigationManager.goToVehicleFormFragment(supportFragmentManager, args)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let {

            val latitude = it[0] as String
            val longitude = it[1] as String

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude")
            )

            startActivity(intent)
        }
    }
}
