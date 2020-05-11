package com.example.projetocm_g11.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.projetocm_g11.R
import com.example.projetocm_g11.data.local.entities.ParkingLot
import com.example.projetocm_g11.data.local.entities.Type
import com.example.projetocm_g11.ui.listeners.OnTouchEvent
import kotlinx.android.synthetic.main.parking_lots_landscape_list_item.view.*
import java.text.SimpleDateFormat

class ParkingLotLandscapeAdapter(private val listener: OnTouchEvent, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    ParkingLotPortraitAdapter(listener, context, layout, items) {

    private val TAG = ParkingLotLandscapeAdapter::class.java.simpleName

    class ParkingLotsLandscapeViewHolder(view: View) : ParkingLotPortraitAdapter.ParkingLotsPortraitViewHolder(view) {

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

                                listener.onSwipeEvent(items[position], 1)
                                Log.i(TAG, "SWIPE RIGHT")
                            }
                            onTouchX - 10 > event.x -> {

                                listener.onSwipeEvent(items[position], 0)
                                Log.i(TAG, "SWIPE LEFT")
                            }
                            else -> {

                                v?.performClick()
                                Log.i(TAG, "CLICKED")
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