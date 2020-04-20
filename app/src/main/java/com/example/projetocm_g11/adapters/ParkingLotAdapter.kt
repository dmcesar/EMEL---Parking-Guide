package com.example.projetocm_g11.adapters

import android.content.Context
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.interfaces.OnClickEvent
import kotlinx.android.synthetic.main.parking_lots_list_item.view.*

class ParkingLotAdapter(private val listener: OnClickEvent, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val capacityBar: ProgressBar = view.park_capacity_bar
        val capacityText: TextView = view.park_capacity_text

        val name: TextView = view.park_name
        val occupancyState: TextView = view.park_occupancy_state
        val availability: TextView = view.park_availability

        //val coordinates: TextView = view.park_coordinates
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        return HistoryViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val capacity = "${items[position].capacityPercent}%"
        val coordinates = "(${items[position].latitude}, ${items[position].longitude})"

        val availability = if(items[position].active) "Open" else "Closed"

        holder.capacityBar.progress = items[position].capacityPercent
        holder.capacityText.text = capacity
        holder.name.text = items[position].name
        holder.occupancyState.text = items[position].getState()
        holder.availability.text = availability
        //holder.coordinates.text = coordinates

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }
    }
}