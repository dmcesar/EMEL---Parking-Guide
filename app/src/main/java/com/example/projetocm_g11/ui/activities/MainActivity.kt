package com.example.projetocm_g11.ui.activities

import android.content.Context
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
import com.example.projetocm_g11.ui.fragments.ContactsFragment
import com.example.projetocm_g11.ui.fragments.ParkingLotsListFragment
import com.example.projetocm_g11.ui.fragments.SettingsFragment
import com.example.projetocm_g11.ui.fragments.VehiclesListFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

const val EXTRA_THEME = "com.example.projetocm_g11.ui.activities.THEME"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNavigateToFragment {

    private val TAG = MainActivity::class.java.simpleName

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Navigate to selected fragment
        when(item.itemId) {

            R.id.nav_parkings_lots -> NavigationManager.goToFragment(supportFragmentManager,
                ParkingLotsListFragment()
            )

            R.id.nav_vehicles -> NavigationManager.goToFragment(supportFragmentManager,
                VehiclesListFragment()
            )

            R.id.nav_contacts -> NavigationManager.goToFragment(supportFragmentManager,
                ContactsFragment()
            )

            R.id.nav_settings -> NavigationManager.goToFragment(supportFragmentManager,
                SettingsFragment()
            )

            R.id.nav_quit -> finish()
        }

        // Close drawer
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

            else -> super.onBackPressed()
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

    private fun launchThemeThread() {

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE) ?: return

        val dateFormatter = SimpleDateFormat("HH:mm")

        val morningDate = dateFormatter.parse("08:00")
        val afternoonDate = dateFormatter.parse("16:00")

        /* Checks every 5 minutes */
        val millisToSleep: Long = 1000 * 20

        CoroutineScope(Dispatchers.Default).launch {
            while(true) {

                Log.i(TAG, "Validating theme according to current time")

                // TODO: Check battery level

                /* Check what time it is */
                val currentHour = dateFormatter.format(Date())

                Log.i(TAG, "Current time is: $currentHour")

                val currentDate = dateFormatter.parse(currentHour)

                if (currentDate.after(morningDate) && currentDate.before(afternoonDate)) {

                    /* App should be in LightTheme */

                    Log.i(TAG, "Switching to LightTheme")

                    withContext(Dispatchers.Main) {

                        theme.applyStyle(R.style.LightTheme, true)

                        finish()
                        startActivity(intent)
                        //setTheme(R.style.LightTheme)
                        //recreate()
                    }

                } else {

                    /* App should be in DarkTheme */

                    Log.i(TAG, "Switching to DarkTheme")

                    withContext(Dispatchers.Main) {

                        theme.applyStyle(R.style.DarkTheme, true)

                        //setTheme(R.style.DarkTheme)
                        finish()
                        startActivity(intent)
                    }
                }
                delay(millisToSleep)
            }
        }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i(TAG, "onCreate()")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Provides drawer access within toolbar
        setSupportActionBar(toolbar)

        // Removes title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        if(!screenRotated(savedInstanceState)) {

            /* Launches thread that checks time. When time is between 18:00-8:00, sets App theme to DarkTheme.
            * When time is between 8:00-18:00, sets App Theme to Light Theme */
            launchThemeThread()

            /* Creates and starts ParkingLotsListFragment with previously fetched data */
            initListFragment()
        }
    }

    override fun onNavigateToFragment(fragment: Fragment?) {

        fragment?.let { NavigationManager.goToFragment(supportFragmentManager, it) }
    }
}
