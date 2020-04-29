package com.example.projetocm_g11.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.adapters.FiltersAdapter
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.FilterType
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.interfaces.OnFiltersListReceived
import com.example.projetocm_g11.viewmodel.FiltersViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.*
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.view.*

class ParkingLotsFiltersFragment : Fragment(), OnDataReceived, OnClickEvent {

    private val TAG = ParkingLotsFiltersFragment::class.java.simpleName

    /* Assembles filters list */
    private lateinit var viewModel: FiltersViewModel

    private var listener: OnFiltersListReceived? = null

    @OnClick(R.id.button_toggle_park_type_filter_list)
    fun onClickButtonToggleParkTypeFilterList() {

        if(park_type_filter_list.visibility == View.VISIBLE) {

            park_type_filter_list.visibility = View.GONE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            park_type_filter_list.visibility = View.VISIBLE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_type_surface.setOnCheckedChangeListener { _, isChecked ->

                val filter = Filter(FilterType.TYPE, "SURFACE")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }

            filter_park_type_underground.setOnCheckedChangeListener { _, isChecked ->

                val filter = Filter(FilterType.TYPE, "UNDERGROUND")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }
        }
    }

    @OnClick(R.id.button_toggle_park_availability_filter_list)
    fun onClickButtonToggleParkAvailabilityFilterList() {

        if(park_availability_filter_list.visibility == View.VISIBLE) {

            park_availability_filter_list.visibility = View.GONE

            button_toggle_park_availability_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            park_availability_filter_list.visibility = View.VISIBLE

            button_toggle_park_availability_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_open.setOnCheckedChangeListener{ _, isChecked ->

                val filter = Filter(FilterType.AVAILABILITY, "AVAILABLE")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }

            filter_park_closed.setOnCheckedChangeListener{ _, isChecked ->

                val filter = Filter(FilterType.AVAILABILITY, "UNAVAILABLE")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }
        }
    }

    @OnClick(R.id.button_toggle_park_fair_filter_list)
    fun onClickButtonToggleParkFairFilterList() {

        if(park_fair_filter_list.visibility == View.VISIBLE) {

            park_fair_filter_list.visibility = View.GONE

            button_toggle_park_fair_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            park_fair_filter_list.visibility = View.VISIBLE

            button_toggle_park_fair_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)

            filter_park_fair_green.setOnCheckedChangeListener{ _, isChecked ->

                val filter = Filter(FilterType.FAIR, "GREEN")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }

            filter_park_fair_yellow.setOnCheckedChangeListener{ _, isChecked ->

                val filter = Filter(FilterType.FAIR, "YELLOW")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }

            filter_park_fair_red.setOnCheckedChangeListener{ _, isChecked ->

                val filter = Filter(FilterType.FAIR, "RED")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else this.viewModel.removeFilter(filter)
            }
        }
    }

    @OnClick(R.id.button_toggle_park_distance_filter_list)
    fun onClickButtonToggleParkDistanceFilterList() {

        if(park_distance_filter_list.visibility == View.VISIBLE) {

            park_distance_filter_list.visibility = View.GONE

            button_toggle_park_distance_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            park_distance_filter_list.visibility = View.VISIBLE

            button_toggle_park_distance_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
        }
    }

    @OnClick(R.id.filter_park_closest)
    fun onClickFilterParkClosest() {

        if(filter_park_furthest.isChecked) {

            filter_park_furthest.isChecked = false

            val toRemove = Filter(FilterType.DISTANCE, "FURTHEST")
            this.viewModel.removeFilter(toRemove)
        }

        val toAdd = Filter(FilterType.DISTANCE, "CLOSEST")
        this.viewModel.addFilter(toAdd)
    }

    @OnClick(R.id.filter_park_furthest)
    fun onClickFilterParkFurthest() {

        if(filter_park_closest.isChecked) {

            filter_park_closest.isChecked = false

            val toRemove = Filter(FilterType.DISTANCE, "CLOSEST")
            this.viewModel.removeFilter(toRemove)
        }

        val toAdd = Filter(FilterType.DISTANCE, "FURTHEST")
        this.viewModel.addFilter(toAdd)
    }

    @OnClick(R.id.button_apply_filters)
    fun onClickButtonApplyFilters() {

        if(filter_park_name.text.isNotBlank()) {

            val filter = Filter(FilterType.NAME, filter_park_name.text.toString())

            this.viewModel.addFilter(filter)
        }

        this.listener?.onFiltersListReceived(viewModel.filters)
    }

    fun registerListener(listener: OnFiltersListReceived) {

        this.listener = listener
    }

    private fun handleArgs() {

        /* Retrieve extras */
        val previousFilters = this.arguments?.getParcelableArrayList<Filter>(EXTRA_FILTERS_LIST)
        val resultsSize = this.arguments?.getInt(EXTRA_PARKING_LOTS_LIST_SIZE)

        /* Set results_count text */

        resultsSize?.let {

            val resultsCount = "$resultsSize ${(activity as Context).resources.getString(R.string.results)}"
            results_count.text = resultsCount
        }

        previousFilters?.let {

            /* Init ViewModel's filters list */
            this.viewModel.update(it)
        }
    }

    private fun updateFilters(list: ArrayList<Filter>) {

        filter_park_type_surface.isChecked = list.contains(Filter(FilterType.TYPE, "SURFACE"))

        filter_park_type_underground.isChecked = list.contains(Filter(FilterType.TYPE, "UNDERGROUND"))

        filter_park_open.isChecked = list.contains(Filter(FilterType.AVAILABILITY, "AVAILABLE"))

        filter_park_closed.isChecked = list.contains(Filter(FilterType.AVAILABILITY, "UNAVAILABLE"))

        filter_park_fair_green.isChecked = list.contains(Filter(FilterType.FAIR, "GREEN"))

        filter_park_fair_yellow.isChecked = list.contains(Filter(FilterType.FAIR, "YELLOW"))

        filter_park_fair_red.isChecked = list.contains(Filter(FilterType.FAIR, "RED"))

        filter_park_closest.isChecked = list.contains(Filter(FilterType.DISTANCE, "CLOSEST"))

        filter_park_furthest.isChecked = list.contains(Filter(FilterType.DISTANCE, "FURTHEST"))

        filter_parks_alphabetically.isChecked = list.contains(Filter(FilterType.ALPHABETICAL))

        filters.adapter = FiltersAdapter(
            this,
            activity as Context,
            R.layout.filters_list_item,
            list
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            view.filters.layoutManager =
                LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)

        } else view.filters.layoutManager = LinearLayoutManager(activity as Context)

        ButterKnife.bind(this, view)

        this.viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)

        return view
    }

    override fun onStart() {

        this.viewModel.filters.let {

            filters.adapter = FiltersAdapter(
                this,
                activity as Context,
                R.layout.filters_list_item,
                it
            )
        }

        this.viewModel.registerListener(this)

        handleArgs()

        filter_parks_alphabetically.setOnCheckedChangeListener{ _, isChecked ->

            val filter = Filter(FilterType.ALPHABETICAL)

            if(isChecked) {

                this.viewModel.addFilter(filter)

            } else this.viewModel.removeFilter(filter)
        }

        super.onStart()
    }

    override fun onStop() {

        this.viewModel.unregisterListener()
        this.listener = null
        super.onStop()
    }

    private fun onDataChanged(list: ArrayList<Filter>) {

        updateFilters(list)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        Log.i(TAG, "onDataReceived -> ${data?.size}")

        data?.let { onDataChanged(it as ArrayList<Filter>) }
    }

    override fun onClickEvent(data: Any?) {

        data?.let { this.viewModel.removeFilter(it as Filter) }
    }
}