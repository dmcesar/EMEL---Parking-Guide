package com.example.projetocm_g11.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.projetocm_g11.NavigationManager
import com.example.projetocm_g11.OnClickEvent
import com.example.projetocm_g11.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnClickEvent {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Navigate to selected fragment
        when(item.itemId) {
            R.id.nav_contacts -> NavigationManager.goToContactsFragment(supportFragmentManager)
        }

        // Close drawer
        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START)

        else if(supportFragmentManager.backStackEntryCount == 1) finish()

        else super.onBackPressed()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Provides drawer access within toolbar
        setSupportActionBar(toolbar)

        // Removes title from toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupDrawerMenu()

        if(!screenRotated(savedInstanceState)) {
            NavigationManager.goToListFragment(supportFragmentManager)
        }

        // Navigate to list fragment
        NavigationManager.goToListFragment(supportFragmentManager)
    }

    // TODO: Pass API arguments
    override fun onClickEvent() {

        NavigationManager.goToParkInfoFragment(supportFragmentManager)
    }
}
