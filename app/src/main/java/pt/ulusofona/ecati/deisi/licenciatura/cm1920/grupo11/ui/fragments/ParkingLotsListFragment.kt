package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import pt.ulusofona.ecati.deisi.ui.adapters.ParkingLotLandscapeAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters.ParkingLotPortraitAdapter
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities.EXTRA_DATA_FROM_REMOTE
import kotlinx.android.synthetic.main.fragment_parking_lots_list.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnTouchListener
import kotlin.collections.ArrayList

class ParkingLotsListFragment : Fragment(),
    OnTouchListener,
    OnDataReceivedListener {

    private val TAG = ParkingLotsListFragment::class.java.simpleName

    private var listener: OnTouchListener? = null

    private var data: ArrayList<ParkingLot>? = null
    private var dataIsFromRemote: Boolean? = null
    private var dataFetchedBefore: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.arguments?.let {

            this.data = it.getParcelableArrayList(EXTRA_DATA)
            this.dataIsFromRemote = it.getBoolean(EXTRA_DATA_FROM_REMOTE)
            this.dataFetchedBefore = it.getBoolean(EXTRA_DATA_FETCHED_BEFORE)
        }

        this.arguments = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_parking_lots_list, container, false)
    }

    override fun onStart() {

        this.listener = parentFragment as OnTouchListener

        this.data?.let { initAdapter(it) }

        this.dataFetchedBefore?.let { fetchedBefore ->

            if(fetchedBefore) {

                this.dataIsFromRemote?.let { fromRemote ->

                    if(!fromRemote) {

                        AlertDialog.Builder(activity as Context)
                            .setTitle(R.string.outdatedDataTitle)
                            .setMessage(R.string.outdatedDataMessage)
                            .setNegativeButton(R.string.OK, null)
                            .show()
                    }
                }
            }
        }

        super.onStart()
    }

    override fun onStop() {

        this.listener = null

        super.onStop()
    }

    /* Updates RecycleView based on received data and screen orientation */
    private fun initAdapter(data: ArrayList<ParkingLot>) {

        parking_lots.layoutManager = LinearLayoutManager(activity as Context)

        if((activity as Context).resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            parking_lots.adapter =
                ParkingLotPortraitAdapter(
                    this,
                    activity as Context,
                    R.layout.parking_lots_portrait_list_item,
                    data
                )

        } else {

            parking_lots.adapter =
                ParkingLotLandscapeAdapter(
                    this,
                    activity as Context,
                    R.layout.parking_lots_landscape_list_item,
                    data
                )
        }
    }

    override fun onSwipeLeftEvent(data: Any?) {

        this.listener?.onSwipeLeftEvent(data)
    }

    override fun onSwipeRightEvent(data: Any?) {

        this.listener?.onSwipeRightEvent(data)
    }

    /* Action triggered when RecycleView item is clicked */
    override fun onClickEvent(data: Any?) {

        this.listener?.onClickEvent(data)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDataReceived(data: ArrayList<*>?) {

        data?.let { initAdapter(it as ArrayList<ParkingLot>) }
    }
}