package pt.ulusofona.ecati.deisi.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.ulusofona.ecati.deisi.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.data.local.entities.Type
import pt.ulusofona.ecati.deisi.ui.listeners.OnTouchListener
import kotlinx.android.synthetic.main.parking_lots_landscape_list_item.view.*
import pt.ulusofona.ecati.deisi.R
import java.text.SimpleDateFormat

class ParkingLotLandscapeAdapter(private val listener: OnTouchListener, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    ParkingLotPortraitAdapter(listener, context, layout, items) {

    class ParkingLotsLandscapeViewHolder(view: View) : ParkingLotsPortraitViewHolder(view) {

        val coordinates: TextView = view.park_coordinates
        val type: TextView = view.park_type
        val lastUpdatedAt: TextView = view.last_updated_at
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotsLandscapeViewHolder {

        return ParkingLotsLandscapeViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ParkingLotsPortraitViewHolder, position: Int) {

        super.onBindViewHolder(holder, position)

        holder as ParkingLotsLandscapeViewHolder

        val coordinates = "(${items[position].latitude}, ${items[position].longitude})"
        val lastUpdatedAt = "${ context.resources.getString(R.string.last_updated_at) }: " +
                SimpleDateFormat("dd-MM-yyyy").format(items[position].lastUpdatedAt)

        val type = if(items[position].getTypeEnum() == Type.UNDERGROUND)
            context.resources.getString(R.string.type_underground)
        else context.resources.getString(R.string.type_surface)

        holder.coordinates.text = coordinates
        holder.type.text = type
        holder.lastUpdatedAt.text = lastUpdatedAt

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {

            var onTouchX = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {

                        onTouchX = event.x
                    }

                    MotionEvent.ACTION_UP -> {

                        when {
                            onTouchX + 10 < event.x -> {

                                listener.onSwipeRightEvent(items[position])
                            }
                            onTouchX - 10 > event.x -> {

                                listener.onSwipeLeftEvent(items[position])
                            }
                            else -> {

                                v?.performClick()
                            }
                        }
                    }

                    else -> return true
                }

                return true
            }
        })
    }
}