package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.parking_lots_portrait_list_item.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnTouchListener
import java.math.RoundingMode

open class ParkingLotPortraitAdapter(private val listener: OnTouchListener, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotPortraitAdapter.ParkingLotsPortraitViewHolder>() {

    private val TAG = ParkingLotPortraitAdapter::class.java.simpleName

    open class ParkingLotsPortraitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val capacityBar: ProgressBar = view.park_capacity_bar
        val capacityText: TextView = view.park_capacity_text

        val name: TextView = view.park_name
        val occupancyState: TextView = view.park_occupancy_state
        val availability: TextView = view.park_availability
        val distance: TextView = view.park_distance
        val isFavorite: ImageView = view.park_isFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingLotsPortraitViewHolder {

        return ParkingLotsPortraitViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ParkingLotsPortraitViewHolder, position: Int) {

        val capacity = "${items[position].getCapacityPercent()}%"
        val state = when {

            items[position].getCapacityPercent() == 100 -> {

                context.resources.getString(R.string.state_full)
            }

            items[position].getCapacityPercent() >= 90 -> {

                context.resources.getString(R.string.state_potentially_full)
            }

            else -> context.resources.getString(R.string.state_free)
        }

        val availability = if (items[position].active == 1)
            context.resources.getString(R.string.park_open)
        else context.resources.getString(R.string.park_closed)

        val distance = context.resources.getString(R.string.distance) + ": " + (items[position].distanceToUser / 1000).toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString() + context.resources.getString(R.string.kilometers)

        holder.capacityBar.progress = items[position].getCapacityPercent()
        holder.capacityText.text = capacity
        holder.name.text = items[position].name
        holder.occupancyState.text = state
        holder.availability.text = availability
        holder.isFavorite.visibility = if(items[position].isFavourite) { View.VISIBLE } else View.GONE
        holder.distance.text = distance

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {

            var onTouchX = 0f
            var swipedDistance = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    /* Save pressed X coordinate */
                    MotionEvent.ACTION_DOWN -> {

                        onTouchX = event.x
                    }

                    /* Calculate swiped distance */
                    MotionEvent.ACTION_MOVE -> {

                        swipedDistance = event.x - onTouchX

                        /* Check if swipe distance is valid */
                        if (swipedDistance < 1000 && swipedDistance > -1000) {

                            ObjectAnimator.ofFloat(
                                holder.itemView,
                                "translationX",
                                swipedDistance
                            ).apply {
                                start()
                            }

                            /* Check swipe direction */
                            if (swipedDistance >= 500) {

                                listener.onSwipeRightEvent(items[position])

                            } else if (swipedDistance <= -500) {

                                listener.onSwipeLeftEvent(items[position])
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {

                        /* Reset animation */
                        ObjectAnimator.ofFloat(
                            holder.itemView,
                            "translationX",
                            0f
                        ).apply {
                            start()
                        }

                        if (swipedDistance <= 20 && swipedDistance >= -20) {

                            v?.performClick()
                        }
                    }

                    MotionEvent.ACTION_CANCEL -> {

                        /* Reset animation when action is canceled */
                        ObjectAnimator.ofFloat(
                            holder.itemView,
                            "translationX",
                            0f
                        ).apply {
                            start()
                        }
                    }

                    else -> return true
                }

                return true
            }
        })
    }
}