package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments.*

/* Handles navigation for MainActivity */
class NavigationManager {

    companion object {

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {

            val transition = fm.beginTransaction()
            transition.replace(R.id.frame, fragment)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun goToParkingLotsFragment(fm: FragmentManager, args: Bundle?) {

            val fragment =
                ParkingLotsFragment()
            args?.let { fragment.arguments = it }

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToVehiclesListFragment(fm: FragmentManager) {

            val fragment =
                VehiclesListFragment()

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToContactsFragment(fm: FragmentManager) {

            val fragment =
                ContactsFragment()

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToSettingsFragment(fm: FragmentManager) {

            val fragment =
                SettingsFragment()

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToParkingLotDetailsFragment(fm: FragmentManager, args: Bundle) {

            val fragment =
                ParkingLotDetailsFragment()
            fragment.arguments = args

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToVehicleFormFragment(fm: FragmentManager, args: Bundle?) {

            val fragment =
                VehicleFormFragment()
            args?.let { fragment.arguments = it }

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToFiltersFragment(fm: FragmentManager) {

            val fragment =
                ParkingLotsFiltersFragment()

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToParkingZoneDetailsFragment(fm: FragmentManager, args: Bundle) {

            val fragment = ParkingZoneDetailsFragment()
            fragment.arguments = args

            placeFragment(fm, fragment)
        }
    }
}