package com.example.projetocm_g11.ui.listeners

import android.os.Bundle

interface OnNavigationListener {

    fun onNavigateToParkingLotDetails(args: Bundle)

    fun onNavigateToParkingLotNavigation(args: Bundle)

    fun onNavigateToFiltersFragment()

    fun onNavigateToVehicleForm(args: Bundle?)
}