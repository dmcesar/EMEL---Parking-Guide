package pt.ulusofona.ecati.deisi.ui.listeners

import android.os.Bundle

interface OnNavigationListener {

    fun onNavigateToParkingLotDetails(args: Bundle)

    fun onNavigateToParkingLotNavigation(args: Bundle)

    fun onNavigateToFiltersFragment()

    fun onNavigateToVehicleForm(args: Bundle?)
}