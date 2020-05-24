package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

import android.os.Bundle

interface OnNavigationListener {

    fun onNavigateToParkingLotDetails(args: Bundle)

    fun onNavigateToParkingLotNavigation(args: Bundle)

    fun onNavigateToFiltersFragment()

    fun onNavigateToVehicleForm(args: Bundle?)
}