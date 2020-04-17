package com.example.projetocm_g11

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.projetocm_g11.view.ParkingLotsListFragment
import com.example.projetocm_g11.view.VehicleFormFragment
import com.example.projetocm_g11.view.VehiclesListFragment

class NavigationManager {

    // Handles Fragment navigation
    companion object {

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {

            val transition = fm.beginTransaction()
            transition.replace(R.id.frame, fragment)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun goToFragment(fm: FragmentManager, fragment: Fragment) {

            placeFragment(fm, fragment)
        }
    }
}