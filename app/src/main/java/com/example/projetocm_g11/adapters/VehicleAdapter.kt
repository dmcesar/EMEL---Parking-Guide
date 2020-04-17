package com.example.projetocm_g11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.domain.data.Vehicle
import kotlinx.android.synthetic.main.vehicles_list_item.view.*

class VehicleAdapter(private val context: Context, private val layout: Int, private val items: MutableList<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val makeModel: TextView = view.make_model
        val licencePlate: TextView = view.licence_plate
        val plateDate: TextView = view.plate_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        return HistoryViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val makeModel = items[position].brand + " : " + items[position].model

        holder.makeModel.text = makeModel
        holder.licencePlate.text = items[position].plate
        holder.plateDate.text = items[position].getDate()

        //holder.itemView.setOnClickListener { listener.onClickEvent() }
    }
}