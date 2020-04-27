package com.example.projetocm_g11.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.projetocm_g11.interfaces.OnDataReceived

import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnClickEvent
import com.example.projetocm_g11.interfaces.OnNavigateToFragment
import com.example.projetocm_g11.viewmodel.ParkingLotsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_parking_lots_main.*
import kotlin.collections.ArrayList

const val EXTRA_PARKING_LOT = "com.example.projetocm_g11.view.ParkingLotsListFragment.ParkingLot"

class ParkingLotsMainFragment : Fragment(), OnDataReceived, OnClickEvent {

    private var navigationListener: OnNavigateToFragment? = null
    private var onDataReceivedListener: OnDataReceived? = null

    private lateinit var viewModel: ParkingLotsViewModel

    private lateinit var parkingLotsListFragment: ParkingLotsListFragment
    lateinit var parkingLotsFiltersFragment: ParkingLotsFiltersFragment

    @OnClick(R.id.button_filter)
    fun showPopupFilter() {

        this.parkingLotsFiltersFragment = ParkingLotsFiltersFragment()

        val transition = childFragmentManager.beginTransaction().add(R.id.fragment_container, this.parkingLotsFiltersFragment)
        transition.addToBackStack(null)
        transition.commit()

        (activity as MainActivity).background_blur.visibility = View.VISIBLE
        (activity as MainActivity).background_blur.setOnClickListener {

            (activity as MainActivity).background_blur.visibility = View.GONE
            removeFiltersFragment()
        }
    }

    fun removeFiltersFragment() {

        childFragmentManager.beginTransaction().remove(this.parkingLotsFiltersFragment).commit()
        childFragmentManager.popBackStack()
    }

    private fun registerListeners() {

        this.navigationListener = activity as OnNavigateToFragment
        this.onDataReceivedListener = this.parkingLotsListFragment
        this.parkingLotsListFragment.registerListener(this)
    }

    private fun unregisterListeners() {

        this.navigationListener = null
        this.parkingLotsListFragment.unregisterListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_main, container, false)

        this.viewModel = ViewModelProviders.of(this).get(ParkingLotsViewModel::class.java)

        ButterKnife.bind(this, view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.parkingLotsListFragment = ParkingLotsListFragment()

        childFragmentManager.beginTransaction().replace(R.id.fragment_container, this.parkingLotsListFragment).commit()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {

        registerListeners()

        this.viewModel.registerListener(this)
        super.onStart()
    }

    override fun onStop() {

        unregisterListeners()

        this.viewModel.unregisterListener()
        super.onStop()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(list: ArrayList<*>) {

        this.onDataReceivedListener?.onDataReceived(list)
    }

    override fun onClickEvent(data: Any) {

        val itemDetail = ParkingLotInfoFragment()

        val args = Bundle()
        args.putParcelable(EXTRA_PARKING_LOT, data as ParkingLot)
        itemDetail.arguments = args

        this.navigationListener?.onNavigateToFragment(itemDetail)
    }
}