package com.example.projetocm_g11.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick

import com.example.projetocm_g11.R
import kotlinx.android.synthetic.main.fragment_parking_lots_filters.*

class ParkingLotsFiltersFragment : Fragment() {

    @OnClick(R.id.button_toggle_park_type_filter_list)
    fun onClickButtonToggleParkTypeFilterList() {

        if(park_type_filter_list.visibility == View.VISIBLE) {

            park_type_filter_list.visibility = View.GONE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_more, 0, 0, 0)

        } else {

            park_type_filter_list.visibility = View.VISIBLE

            button_toggle_park_type_filter_list.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_less, 0, 0, 0)
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

    @OnClick(R.id.filter_park_fair_green)
    fun onClickFilterParkFairGreen() {

        if(filter_park_fair_yellow.isChecked) {

            filter_park_fair_yellow.isChecked = false
        }

        if(filter_park_fair_red.isChecked) {

            filter_park_fair_red.isChecked = false
        }
    }

    @OnClick(R.id.filter_park_fair_yellow)
    fun onClickFilterParkFairYellow() {

        if(filter_park_fair_green.isChecked) {

            filter_park_fair_green.isChecked = false
        }

        if(filter_park_fair_red.isChecked) {

            filter_park_fair_red.isChecked = false
        }
    }

    @OnClick(R.id.filter_park_fair_red)
    fun onClickFilterParkFairRed() {

        if(filter_park_fair_yellow.isChecked) {

            filter_park_fair_yellow.isChecked = false
        }

        if(filter_park_fair_green.isChecked) {

            filter_park_fair_green.isChecked = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parking_lots_filters, container, false)

        ButterKnife.bind(this, view)

        return view
    }
}