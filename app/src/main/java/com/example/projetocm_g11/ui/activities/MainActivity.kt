package com.example.projetocm_g11.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.projetocm_g11.ui.utils.NavigationManager
import com.example.projetocm_g11.ui.listeners.OnNavigateToFragment
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.sensors.battery.Battery
import com.example.projetocm_g11.data.sensors.battery.OnBatteryCapacityListener
import com.example.projetocm_g11.ui.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

const val PREFERENCE_APPLIED_THEME = "com.example.projetocm_g11.ui.activities.APPLIED_THEME"
const val PREFERENCE_QUEUED_THEME = "com.example.projetocm_g11.ui.activities.QUEUED_THEME"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNavigateToFragment, OnBatteryCapacityListener {

    private val TAG = MainActivity::class.java.simpleName

    private fun initListFragment() {

        val data = intent?.getParcelableArrayListExtra<ParkingLot>(EXTRA_PARKING_LOTS)
        val updated = intent?.getBooleanExtra(EXTRA_UPDATED, false)

        val args = Bundle()

        args.putParcelableArrayList(EXTRA_PARKING_LOTS, data)

        if (updated != null) {
            args.putBoolean(EXTRA_UPDATED, updated)
        }

        val fragment = ParkingLotsListFragment()
        fragment.arguments = args

        // Navigate to list fragment
        NavigationManager.goToFragment(supportFragmentManager,
            fragment
        )
    }

    /* Called when navigation occurs.
    * Validates which theme should be applied, according to current time.
    * If a switch should occur, queue theme ID and recreate activity to apply it. */
    @SuppressLint("SimpleDateFormat")
    private fun validateThemeTime() {

        Log.i(TAG, "Validate by time")

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
        Log.i(TAG, "Current time is: $currentHour")

        /* If current theme is LightTheme*/
        if(currentTheme == R.style.LightTheme) {

            /* Check if should be in DarkTheme */
            if(currentHour.before(morningDate) || currentHour.after(afternoonDate)) {

                /* Queue DarkTheme */
                Log.i(TAG, "Queued DarkTheme")
                sharedPreferences.edit().putInt(PREFERENCE_QUEUED_THEME, R.style.DarkTheme).apply()
            }
        }

        /* If current theme is DarkTheme */
        else if(currentTheme == R.style.DarkTheme) {

            /* Check if should be in LightTheme */
            if(currentHour.after(morningDate) && currentHour.before(afternoonDate)) {

                /* Queue LightTheme */
                Log.i(TAG, "Queued LightTheme")
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
            if (!notifyBatteryLow && capacity > 20) {

                Log.i(
                    TAG,
                    "Battery recharged. Notifying user again when battery is lower than 20%."
                )

                sharedPreferences.edit().putBoolean(PREFERENCE_SWITCH_THEMES_NOTIFY, true)
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

                                Log.i(TAG, "Queued DarkTheme")

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

            Log.i(TAG, "Recreating activity")

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

        /* Navigate to selected fragment */
        when(item.itemId) {

            R.id.nav_parkings_lots -> {

                NavigationManager.goToFragment(supportFragmentManager,
                    ParkingLotsListFragment()
                )

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_vehicles -> {

                NavigationManager.goToFragment(supportFragmentManager,
                    VehiclesListFragment()
                )

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_contacts -> {

                NavigationManager.goToFragment(supportFragmentManager,
                    ContactsFragment()
                )

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_settings -> {

                NavigationManager.goToFragment(supportFragmentManager,
                    SettingsFragment()
                )

                validateThemeTime()
                validateThemes()
            }

            R.id.nav_quit -> finish()
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

        // Provides drawer access within toolbar
        setSupportActionBar(toolbar)

        // Removes title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        if(!screenRotated(savedInstanceState)) {

            /* Creates and starts ParkingLotsListFragment with previously fetched data */
            initListFragment()
        }

    }

    override fun onStart() {

        /* Notify activity as Battery capacity listener */
        Battery.registerListener(this)

        super.onStart()
    }

    override fun onStop() {

        Battery.unregisterListener()

        super.onStop()
    }

    override fun onNavigateToFragment(fragment: Fragment?) {

        validateThemeTime()
        validateThemes()

        /* Navigate to fragment and update theme according to current time */
        fragment?.let {

            NavigationManager.goToFragment(supportFragmentManager, it)
        }
    }

    override fun onBatteryCapacityListener(capacity: Int) {

        validateThemeBattery(capacity)
    }
}
