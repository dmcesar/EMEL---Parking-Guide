package com.example.projetocm_g11.ui.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.projetocm_g11.R
import com.example.projetocm_g11.ui.fragments.ParkingLotsListFragment
import com.example.projetocm_g11.ui.fragments.ParkingLotsMapFragment

/* Handles navigation for ParkingLotsFragment */

class ParkingLotsNavigationManager {

    companion object {

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {

            val transition = fm.beginTransaction()
            transition.replace(R.id.parking_lots_frame, fragment)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun goToListFragment(fm: FragmentManager, args: Bundle?) {

            val fragment = ParkingLotsListFragment()
            args?.let { fragment.arguments = it }

            placeFragment(
                fm,
                fragment
            )
        }

        fun goToMapFragment(fm: FragmentManager, args: Bundle?) {

            val fragment = ParkingLotsMapFragment()
            args?.let { fragment.arguments = it }

            placeFragment(
                fm,
                fragment
            )
        }
    }
}