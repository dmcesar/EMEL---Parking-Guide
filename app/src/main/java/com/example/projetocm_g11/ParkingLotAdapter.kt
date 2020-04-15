package com.example.projetocm_g11

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.domain.data.ParkingLot
import kotlinx.android.synthetic.main.item_list.view.*

class ParkingLotAdapter(private val listener: OnClickEvent, private val context: Context, private val layout: Int, private val items: MutableList<ParkingLot>) :
    RecyclerView.Adapter<ParkingLotAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val capacityBar: ProgressBar = view.park_capacity_bar
        val capacityText: TextView = view.park_capacity_text

        val name: TextView = view.park_name
        val distance: TextView = view.park_distance
        val availability: TextView = view.park_availability

        //val address: TextView = view.text_result
        //val lastUpdatedAt: TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        return HistoryViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    // TODO: Pass API arguments
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val capacity = "${items[position].capacity}%"

        holder.capacityBar.progress = items[position].capacity
        holder.capacityText.text = capacity
        holder.name.text = items[position].name
        holder.distance.text = "Distance"
        holder.availability.text = items[position].getState()

        holder.itemView.setOnClickListener { listener.onClickEvent() }
    }
}