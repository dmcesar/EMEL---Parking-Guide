package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.map

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Type
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnMapMarkersReceivedListener
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils.Extensions

class MapLogic(private val context: Context) {

    private var listener: OnMapMarkersReceivedListener? = null

    fun registerListener(listener: OnMapMarkersReceivedListener) {

        this.listener = listener
    }

    fun unregisterListener() {

        this.listener = null
    }

    fun getParkingLotPins(parkingLots: ArrayList<ParkingLot>) {

        CoroutineScope(Dispatchers.Default).launch {

            val markers = HashMap<ParkingLot, MarkerOptions>()

            parkingLots.forEach { p ->

                /* Create marker with park coordinates and name */
                val marker = MarkerOptions()
                    .title(p.name)
                    .position(LatLng(p.latitude.toDouble(), p.longitude.toDouble()))

                if (p.getCapacityPercent() < 90) {

                    if (p.getTypeEnum() == Type.UNDERGROUND) {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_free_underground
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_free_underground_closed
                                )
                            )
                        }

                    } else {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_free
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_free_closed
                                )
                            )
                        }
                    }

                }

                else if (p.getCapacityPercent() in 90..99) {

                    if (p.getTypeEnum() == Type.UNDERGROUND) {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_potentially_full_underground
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_potentially_full_underground_closed
                                )
                            )
                        }

                    } else {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_potentially_full
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_potentially_full_closed
                                )
                            )
                        }
                    }

                }

                else {

                    if (p.getTypeEnum() == Type.UNDERGROUND) {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_full_underground
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_full_underground_closed
                                )
                            )
                        }

                    } else {

                        if (p.active == 1) {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_full
                                )
                            )

                        } else {

                            marker.icon(
                                Extensions.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_map_marker_full_closed
                                )
                            )
                        }
                    }
                }

                markers[p] = marker
            }

            withContext(Dispatchers.Main) {

                listener?.onMapMarkersReceived(markers)
            }
        }
    }
}