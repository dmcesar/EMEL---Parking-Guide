package com.example.projetocm_g11

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.projetocm_g11.view.ContactsFragment
import com.example.projetocm_g11.view.ListFragment

abstract class NavigationManager {

    companion object {

        private fun placeFragment(fm: FragmentManager, fragment: Fragment) {

            val transition = fm.beginTransaction()
            transition.replace(R.id.frame, fragment)
            transition.addToBackStack(null)
            transition.commit()
        }

        fun goToListFragment(fm: FragmentManager) {

            placeFragment(fm, ListFragment())
        }

        // TODO: Pass API arguments
        fun goToParkInfoFragment(fm: FragmentManager) {

            //placeFragment(fm, )
        }

        fun goToContactsFragment(fm: FragmentManager) {

            placeFragment(fm, ContactsFragment())
        }
    }
}