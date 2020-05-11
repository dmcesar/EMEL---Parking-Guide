package com.example.projetocm_g11.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.data.local.entities.Vehicle
import com.example.projetocm_g11.ui.listeners.OnClickEvent
import kotlinx.android.synthetic.main.vehicles_list_item.view.*

class VehicleAdapter(private val listener: OnClickEvent, private val context: Context, private val layout: Int, private val items: MutableList<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.VehiclesViewHolder>() {

    class VehiclesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val deleteBut: ImageView = view.button_delete
        val brand: TextView = view.brand
        val model: TextView = view.model
        val licencePlate: TextView = view.plate
        val plateDate: TextView = view.plate_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {

        return VehiclesViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {

        holder.brand.text = items[position].brand
        holder.model.text = items[position].model
        holder.licencePlate.text = items[position].plate
        holder.plateDate.text = items[position].getDate()

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }
        holder.deleteBut.setOnClickListener {  listener.onClickEvent(items[position]) }
    }
}