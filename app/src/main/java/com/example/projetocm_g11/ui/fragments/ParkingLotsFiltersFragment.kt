package com.example.projetocm_g11.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.ui.adapters.FiltersAdapter
import com.example.projetocm_g11.data.local.entities.Filter
import com.example.projetocm_g11.ui.listeners.OnClickListener
import com.example.projetocm_g11.ui.listeners.OnDataReceivedListener
import com.example.projetocm_g11.ui.viewmodels.FiltersViewModel
import com.example.projetocm_g11.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.*
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.view.*

class ParkingLotsFiltersFragment : Fragment(), OnDataReceivedListener, OnClickListener {

    private val TAG = ParkingLotsFiltersFragment::class.java.simpleName

    /* Assembles filters list */
    private lateinit var viewModel: FiltersViewModel

    @OnClick(R.id.button_toggle_park_type_filter_list)
    fun onClickButtonToggleParkTypeFilterList() {

        if(park_type_filter_list.visibility == View.VISIBLE) {

            park_type_filter_list.visibility = View.GONE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            val surfaceFilter = Filter("SURFACE")

            val undergroundFilter = Filter("UNDERGROUND")

            park_type_filter_list.visibility = View.VISIBLE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_type_surface.setOnCheckedChangeListener { _, isChecked ->

                Log.i(TAG, "SURFACE LISTENER CALLED")

                if(isChecked) {

                    this.viewModel.insert(surfaceFilter)

                } else this.viewModel.delete(surfaceFilter)
            }

            filter_park_type_underground.setOnCheckedChangeListener { _, isChecked ->

                Log.i(TAG, "UNDERGROUND LISTENER CALLED")

                if(isChecked) {

                    this.viewModel.insert(undergroundFilter)

                } else this.viewModel.delete(undergroundFilter)
            }
        }
    }

    @OnClick(R.id.button_apply_filters)
    fun onClickButtonApplyFilters() {

        if(filter_park_name.text.isNotBlank()) {

            val filter = Filter(filter_park_name.text.toString())

            this.viewModel.insert(filter)
        }

        (activity as MainActivity).onBackPressed()
    }

    private fun updateFilters(list: ArrayList<Filter>) {

        list.forEach { f->Log.i(TAG, f.toString())}

        filter_park_type_surface.isChecked = list.contains(Filter("SURFACE"))

        filter_park_type_underground.isChecked = list.contains(Filter("UNDERGROUND"))

        filter_park_open.isChecked = list.contains(Filter("AVAILABLE"))

        filter_park_closest.isChecked = list.contains(Filter("CLOSEST"))

        filter_parks_favorites.isChecked = list.contains(Filter("FAVORITE"))

        updateAdapter(list)
    }

    private fun updateAdapter(list: ArrayList<Filter>) {

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            filters.adapter = FiltersAdapter(
                this,
                activity as Context,
                R.layout.filters_list_item_portrait,
                list
            )

        } else {

            filters.adapter = FiltersAdapter(
                this,
                activity as Context,
                R.layout.filters_list_item_landscape,
                list
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        ButterKnife.bind(this, view)

        this.viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            view.filters.layoutManager =
                LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)

        } else view.filters.layoutManager = LinearLayoutManager(activity as Context)

        view.filter_park_open.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter("AVAILABLE")

            if(isChecked) {

                this.viewModel.insert(filter)

            } else this.viewModel.delete(filter)
        }

        view.filter_park_closest.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter("CLOSEST")

            if(isChecked) {

                this.viewModel.insert(filter)

            } else this.viewModel.delete(filter)
        }

        view.filter_parks_favorites.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter("FAVORITE")

            if(isChecked) {

                this.viewModel.insert(filter)

            } else this.viewModel.delete(filter)
        }

        return view
    }

    override fun onStart() {

        this.viewModel.filters.let { updateAdapter(it) }

        this.viewModel.registerListener(this)

        this.viewModel.read()

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()

        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { updateFilters(it as ArrayList<Filter>) }
    }

    override fun onClickEvent(data: Any?) {

        data?.let { this.viewModel.delete(it as Filter) }
    }
}