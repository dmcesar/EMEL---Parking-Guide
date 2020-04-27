package com.example.projetocm_g11.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.projetocm_g11.NavigationManager
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNavigateToFragment {

    private lateinit var parkingLotsMainFragment: ParkingLotsMainFragment

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Navigate to selected fragment
        when(item.itemId) {

            R.id.nav_parkings_lots -> {

                /* Resets Fragment */
                this.parkingLotsMainFragment = ParkingLotsMainFragment()

                NavigationManager.goToFragment(supportFragmentManager, this.parkingLotsMainFragment)
            }

            R.id.nav_vehicles -> NavigationManager.goToFragment(supportFragmentManager, VehiclesListFragment())

            R.id.nav_contacts -> NavigationManager.goToFragment(supportFragmentManager, ContactsFragment())

            R.id.nav_settings -> NavigationManager.goToFragment(supportFragmentManager, SettingsFragment())

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

            /* If Fragment's child Fragment is on the stack (filters Fragment), remove the Fragment */
            this.parkingLotsMainFragment.childFragmentManager.backStackEntryCount == 1 ->
                this.parkingLotsMainFragment.removeFiltersFragment()

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Provides drawer access within toolbar
        setSupportActionBar(toolbar)

        // Removes title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        this.parkingLotsMainFragment = ParkingLotsMainFragment()

        if(!screenRotated(savedInstanceState)) {

            // Navigate to list fragment
            NavigationManager.goToFragment(supportFragmentManager, this.parkingLotsMainFragment)
        }
    }

    override fun onNavigateToFragment(fragment: Fragment) {

        NavigationManager.goToFragment(supportFragmentManager, fragment)
    }
}
