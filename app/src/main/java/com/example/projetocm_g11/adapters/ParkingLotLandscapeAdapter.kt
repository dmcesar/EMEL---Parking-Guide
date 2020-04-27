package com.example.projetocm_g11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Type
import com.example.projetocm_g11.interfaces.OnClickEvent
import kotlinx.android.synthetic.main.parking_lots_landscape_list_item.view.*
import java.text.SimpleDateFormat

class ParkingLotLandscapeAdapter(private val listener: OnClickEvent, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    ParkingLotPortraitAdapter(listener, context, layout, items) {

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

        val type = if(items[position].type == Type.UNDERGROUND)
            context.resources.getString(R.string.type_underground)
        else context.resources.getString(R.string.type_surface)

        holder.coordinates.text = coordinates
        holder.type.text = type
        holder.lastUpdatedAt.text = lastUpdatedAt

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }
    }
}