package com.example.projetocm_g11.view

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
import com.example.projetocm_g11.adapters.FiltersAdapter
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.FilterType
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.viewmodel.FiltersViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.*
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.view.*

class ParkingLotsFiltersFragment : Fragment(), OnDataReceived, OnClickEvent {

    private val TAG = ParkingLotsFiltersFragment::class.java.simpleName

    private lateinit var viewModel: FiltersViewModel

    @OnClick(R.id.button_toggle_park_type_filter_list)
    fun onClickButtonToggleParkTypeFilterList() {

        if(park_type_filter_list.visibility == View.VISIBLE) {

            park_type_filter_list.visibility = View.GONE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            val surfaceFilter = Filter(FilterType.TYPE, "SURFACE")

            val undergroundFilter = Filter(FilterType.TYPE, "UNDERGROUND")

            park_type_filter_list.visibility = View.VISIBLE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_type_surface.setOnCheckedChangeListener { _, isChecked ->

                if(isChecked) {

                    /* Add this filter */
                    this.viewModel.addFilter(surfaceFilter)

                    /* Remove UNDERGROUND if checked */
                    if(filter_park_type_underground.isChecked) {

                        filter_park_type_underground.isChecked = false
                        this.viewModel.removeFilter(undergroundFilter)
                    }

                } else this.viewModel.removeFilter(surfaceFilter)
            }

            filter_park_type_underground.setOnCheckedChangeListener { _, isChecked ->

                if(isChecked) {

                    /* Add this filter */
                    this.viewModel.addFilter(undergroundFilter)

                    /* Remove UNDERGROUND if checked */
                    if(filter_park_type_surface.isChecked) {

                        this.viewModel.removeFilter(surfaceFilter)
                        filter_park_type_surface.isChecked = false
                    }

                } else this.viewModel.removeFilter(undergroundFilter)
            }
        }
    }

    @OnClick(R.id.button_toggle_park_availability_filter_list)
    fun onClickButtonToggleParkAvailabilityFilterList() {

        if(park_availability_filter_list.visibility == View.VISIBLE) {

            park_availability_filter_list.visibility = View.GONE

            button_toggle_park_availability_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            val availableFilter = Filter(FilterType.AVAILABILITY, "AVAILABLE")

            val unavailableFilter = Filter(FilterType.AVAILABILITY, "UNAVAILABLE")

            park_availability_filter_list.visibility = View.VISIBLE

            button_toggle_park_availability_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_open.setOnCheckedChangeListener{ _, isChecked ->

                if(isChecked) {

                    this.viewModel.addFilter(availableFilter)

                    if(filter_park_closed.isChecked) {

                        this.viewModel.removeFilter(unavailableFilter)
                        filter_park_closed.isChecked = false
                    }

                } else this.viewModel.removeFilter(availableFilter)
            }

            filter_park_closed.setOnCheckedChangeListener{ _, isChecked ->

                if(isChecked) {

                    /* Add this filter */
                    this.viewModel.addFilter(unavailableFilter)

                    /* Remove UNDERGROUND if checked */
                    if(filter_park_open.isChecked) {

                        this.viewModel.removeFilter(availableFilter)
                        filter_park_open.isChecked = false
                    }

                } else this.viewModel.removeFilter(unavailableFilter)
            }
        }
    }

    @OnClick(R.id.button_toggle_park_distance_filter_list)
    fun onClickButtonToggleParkDistanceFilterList() {

        if(park_distance_filter_list.visibility == View.VISIBLE) {

            park_distance_filter_list.visibility = View.GONE

            button_toggle_park_distance_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            val closestFilter = Filter(FilterType.DISTANCE, "CLOSEST")

            val furthestFilter = Filter(FilterType.DISTANCE, "FURTHEST")

            park_distance_filter_list.visibility = View.VISIBLE

            button_toggle_park_distance_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_closest.setOnCheckedChangeListener{ _, isChecked ->

                if(isChecked) {

                    /* Add this filter */
                    this.viewModel.addFilter(closestFilter)

                    /* Remove UNDERGROUND if checked */
                    if(filter_park_furthest.isChecked) {

                        this.viewModel.removeFilter(furthestFilter)
                        filter_park_furthest.isChecked = false
                    }

                } else this.viewModel.removeFilter(closestFilter)
            }

            filter_park_furthest.setOnCheckedChangeListener{ _, isChecked ->

                if(isChecked) {

                    /* Add this filter */
                    this.viewModel.addFilter(furthestFilter)

                    /* Remove UNDERGROUND if checked */
                    if(filter_park_closest.isChecked) {

                        this.viewModel.removeFilter(closestFilter)
                        filter_park_closest.isChecked = false
                    }

                } else this.viewModel.removeFilter(furthestFilter)
            }
        }
    }

    @OnClick(R.id.button_apply_filters)
    fun onClickButtonApplyFilters() {

        Log.i(TAG, "onClickButtonApplyFilters() called")

        if(filter_park_name.text.isNotBlank()) {

            val filter = Filter(FilterType.NAME, filter_park_name.text.toString())

            this.viewModel.addFilter(filter)
        }

        this.viewModel.applyFilters()

        (activity as MainActivity).onBackPressed()
    }

    private fun updateFilters(list: ArrayList<Filter>) {

        Log.i(TAG, "updateFilters() called")

        filter_park_type_surface.isChecked = list.contains(Filter(FilterType.TYPE, "SURFACE"))

        filter_park_type_underground.isChecked = list.contains(Filter(FilterType.TYPE, "UNDERGROUND"))

        filter_park_open.isChecked = list.contains(Filter(FilterType.AVAILABILITY, "AVAILABLE"))

        filter_park_closed.isChecked = list.contains(Filter(FilterType.AVAILABILITY, "UNAVAILABLE"))

        filter_park_closest.isChecked = list.contains(Filter(FilterType.DISTANCE, "CLOSEST"))

        filter_park_furthest.isChecked = list.contains(Filter(FilterType.DISTANCE, "FURTHEST"))

        filter_parks_alphabetically.isChecked = list.contains(Filter(FilterType.ALPHABETICAL))

        filter_parks_favorites.isChecked = list.contains(Filter(FilterType.FAVORITE))

        updateAdapter(list)
    }

    private fun updateAdapter(list: ArrayList<Filter>) {

        Log.i(TAG, "updateAdapter() called")

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

        Log.i(TAG, "onCreateView() called")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            view.filters.layoutManager =
                LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)

        } else view.filters.layoutManager = LinearLayoutManager(activity as Context)

        this.viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)

        view.filter_parks_alphabetically.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter(FilterType.ALPHABETICAL)

            if(isChecked) {

                this.viewModel.addFilter(filter)

            } else this.viewModel.removeFilter(filter)
        }

        view.filter_parks_favorites.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter(FilterType.FAVORITE)

            if(isChecked) {

                this.viewModel.addFilter(filter)

            } else this.viewModel.removeFilter(filter)
        }

        ButterKnife.bind(this, view)

        return view
    }

    override fun onStart() {

        Log.i(TAG, "onStart() called")

        this.viewModel.filters.let { updateFilters(it) }

        this.viewModel.registerListeners(this)

        this.viewModel.getFilters()

        super.onStart()
    }

    override fun onDestroy() {

        this.viewModel.unregisterListeners()

        super.onDestroy()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { updateFilters(it as ArrayList<Filter>) }
    }

    override fun onClickEvent(data: Any?) {

        data?.let { this.viewModel.removeFilter(it as Filter) }
    }
}