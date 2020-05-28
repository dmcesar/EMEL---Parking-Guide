package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R

const val PREFERENCE_THEMES = "com.example.projetocm_g11.ui.fragments.EXTRA_PREFERENCE_THEMES"
const val PREFERENCE_FILTERS = "com.example.projetocm_g11.ui.fragments.EXTRA_PREFERENCE_FILTERS"

class SettingsFragment : PreferenceFragmentCompat() {

    private val TAG = SettingsFragment::class.java.simpleName

    private var themesSwitch: SwitchPreference? = null
    private var filtersSwitch: SwitchPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity as Context)

        /* Init themesSwitch with ON state */
        val themesSwitchState = sharedPreferences.getBoolean(PREFERENCE_THEMES, true)

        this.themesSwitch = preferenceManager.findPreference("themes_automatically")

        this.themesSwitch?.isChecked = themesSwitchState

        this.themesSwitch?.onPreferenceChangeListener =

            Preference.OnPreferenceChangeListener { _, newValue ->

                newValue as Boolean

                Log.i(TAG, "Toggled preference: Switch themes automatically to $newValue")

                /* Update shared preferences */
                sharedPreferences.edit().putBoolean(PREFERENCE_THEMES, newValue).apply()

                /* Toggle switch to new value */
                this.themesSwitch?.isChecked = newValue

                newValue
            }


        /* Init filtersSwitch with OFF state */
        val filtersSwitchState = sharedPreferences.getBoolean(PREFERENCE_FILTERS, false)

        this.filtersSwitch = preferenceManager.findPreference("filters")

        this.filtersSwitch?.isChecked = filtersSwitchState

        this.filtersSwitch?.onPreferenceChangeListener =

            Preference.OnPreferenceChangeListener { _, newValue ->

                newValue as Boolean

                Log.i(TAG, "Toggled preference: Delete filters when device is shaken to $newValue")

                /* Update shared preferences */
                sharedPreferences.edit().putBoolean(PREFERENCE_FILTERS, newValue).apply()

                /* Toggle switch to new value */
                this.filtersSwitch?.isChecked = newValue

                newValue
            }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
