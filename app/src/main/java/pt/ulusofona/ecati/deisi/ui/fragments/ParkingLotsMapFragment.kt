package pt.ulusofona.ecati.deisi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pt.ulusofona.ecati.deisi.R
import pt.ulusofona.ecati.deisi.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.ui.activities.EXTRA_DATA

class ParkingLotsMapFragment : Fragment() {

    private var data: ArrayList<ParkingLot>? = null

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {

        return savedInstanceState != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.arguments?.let {

            this.data = it.getParcelableArrayList(EXTRA_DATA)
        }

        this.arguments = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_parking_lots_map, container, false)

        if(!screenRotated(savedInstanceState)) {

            /* Create map fragment */
            val map = MapFragment()

            /* Set list with parking lot as argument */
            val args = Bundle()
            args.putParcelableArrayList(
                EXTRA_DATA,
                data
            )
            map.arguments = args

            /* Place fragment in FrameLayout */
            val transition = this.childFragmentManager.beginTransaction()
            transition.replace(R.id.navigation_map_frame, map)
            transition.addToBackStack(null)
            transition.commit()
        }

        return view
    }
}
