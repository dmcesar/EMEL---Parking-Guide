package com.example.projetocm_g11.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.FilterType
import com.example.projetocm_g11.interfaces.OnDataReceived
import com.example.projetocm_g11.interfaces.OnFiltersListReceived
import com.example.projetocm_g11.viewmodel.FiltersViewModel
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.*
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.view.*

class ParkingLotsFiltersFragment : Fragment() {

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

            filter_park_type_surface.setOnCheckedChangeListener { buttonView, isChecked ->

                val filter = Filter(FilterType.TYPE, "SURFACE")

                this.viewModel.toggleFilter(filter)
            }

            filter_park_type_underground.setOnCheckedChangeListener { buttonView, isChecked ->

                val filter = Filter(FilterType.TYPE, "UNDERGROUND")

                this.viewModel.toggleFilter(filter)
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

            filter_park_open.setOnCheckedChangeListener{ buttonView, isChecked ->

                val filter = Filter(FilterType.AVAILABILITY, "AVAILABLE")

                this.viewModel.toggleFilter(filter)
            }

            filter_park_closed.setOnCheckedChangeListener{ buttonView, isChecked ->

                val filter = Filter(FilterType.AVAILABILITY, "UNAVAILABLE")

                this.viewModel.toggleFilter(filter)
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
        }
    }

    @OnClick(R.id.filter_park_furthest)
    fun onClickFilterParkFurthest() {

        if(filter_park_closest.isChecked) {

            filter_park_closest.isChecked = false
        }
    }

    @OnClick(R.id.button_apply_filters)
    fun onClickButtonApplyFilters() {

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
            this.viewModel.filters = previousFilters

            for(filter in previousFilters) {

                if(filter.type == FilterType.TYPE) {

                    if(filter.value == "SURFACE") {

                        filter_park_type_surface.isChecked = true
                    }

                    else if(filter.value == "UNDERGROUND") {

                        filter_park_type_underground.isChecked = true
                    }
                }

                else if(filter.type == FilterType.AVAILABILITY) {

                    if(filter.value == "AVAILABLE") {

                        filter_park_open.isChecked = true
                    }

                    else if(filter.value == "UNAVAILABLE") {

                        filter_park_closed.isChecked = true
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        ButterKnife.bind(this, view)

        this.viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)

        return view
    }

    override fun onStart() {

        Log.i(TAG, "onStart")

        handleArgs()

        super.onStart()
    }

    override fun onStop() {

        this.listener = null

        super.onStop()
    }
}