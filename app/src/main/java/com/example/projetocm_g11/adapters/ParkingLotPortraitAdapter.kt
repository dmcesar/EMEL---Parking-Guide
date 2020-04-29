package com.example.projetocm_g11.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnTouchEvent
import kotlinx.android.synthetic.main.parking_lots_portrait_list_item.view.*


open class ParkingLotPortraitAdapter(private val listener: OnTouchEvent, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotPortraitAdapter.ParkingLotsPortraitViewHolder>() {

    private val TAG = ParkingLotPortraitAdapter::class.java.simpleName

    open class ParkingLotsPortraitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val capacityBar: ProgressBar = view.park_capacity_bar
        val capacityText: TextView = view.park_capacity_text

        val name: TextView = view.park_name
        val occupancyState: TextView = view.park_occupancy_state
        val availability: TextView = view.park_availability
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

        val availability = if (items[position].active)
            context.resources.getString(R.string.park_open)
        else context.resources.getString(R.string.park_closed)

        holder.capacityBar.progress = items[position].getCapacityPercent()
        holder.capacityText.text = capacity
        holder.name.text = items[position].name
        holder.occupancyState.text = state
        holder.availability.text = availability

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }

        holder.itemView.setOnTouchListener(object : OnTouchListener {

            var onTouchX = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {

                        onTouchX = event.x
                    }

                    MotionEvent.ACTION_UP -> {

                        if(onTouchX < event.x) {

                            listener.onSwipeEvent(items[position])
                            Log.i(TAG, "SWIPE RIGHT")
                        }

                        if(onTouchX == event.x) {
                            v?.performClick()
                            Log.i(TAG, "CLICKED")
                        }
                    }

                    else -> return true
                }

                return true
            }
        })
    }
}