package com.example.projetocm_g11.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.projetocm_g11.R

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

            placeFragment(
                fm,
                fragment
            )
        }
    }
}