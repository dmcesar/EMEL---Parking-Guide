package com.example.projetocm_g11.ui.adapters

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import kotlinx.android.synthetic.main.parking_lots_portrait_list_item.view.*

open class ParkingLotPortraitAdapter(private val listener: com.example.projetocm_g11.ui.listeners.OnTouchListener, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotPortraitAdapter.ParkingLotsPortraitViewHolder>() {

    private val TAG = ParkingLotPortraitAdapter::class.java.simpleName

    open class ParkingLotsPortraitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val capacityBar: ProgressBar = view.park_capacity_bar
        val capacityText: TextView = view.park_capacity_text

        val name: TextView = view.park_name
        val occupancyState: TextView = view.park_occupancy_state
        val availability: TextView = view.park_availability
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

        holder.capacityBar.progress = items[position].getCapacityPercent()
        holder.capacityText.text = capacity
        holder.name.text = items[position].name
        holder.occupancyState.text = state
        holder.availability.text = availability
        holder.isFavorite.visibility = if(items[position].isFavourite) { View.VISIBLE } else View.GONE

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

                                /*val set: AnimatorSet = AnimatorInflater.loadAnimator(context, R.animator.animation_from_right)
                                    .apply {
                                        setTarget(items[position])
                                        start()
                                    } as AnimatorSet

                                 */
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