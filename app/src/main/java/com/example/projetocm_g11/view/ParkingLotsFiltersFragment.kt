package com.example.projetocm_g11.view

import android.os.Bundle
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

class ParkingLotsFiltersFragment : Fragment() {

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

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else {

                    this.viewModel.removeFilter(filter)
                }
            }

            filter_park_type_underground.setOnCheckedChangeListener { buttonView, isChecked ->

                val filter = Filter(FilterType.TYPE, "UNDERGROUND")

                if(isChecked) {

                    this.viewModel.addFilter(filter)

                } else {

                    this.viewModel.removeFilter(filter)
                }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        ButterKnife.bind(this, view)

        this.viewModel = ViewModelProviders.of(this).get(FiltersViewModel::class.java)

        return view
    }

    override fun onStop() {

        this.listener = null

        super.onStop()
    }
}