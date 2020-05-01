package com.example.projetocm_g11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocm_g11.R
import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.FilterType
import com.example.projetocm_g11.interfaces.OnClickEvent
import kotlinx.android.synthetic.main.filters_list_item_portrait.view.*

class FiltersAdapter(private val listener: OnClickEvent, private val context: Context, private val layout: Int, private val items: MutableList<Filter>) :
    RecyclerView.Adapter<FiltersAdapter.FiltersViewHolder>() {

    class FiltersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val filterName: TextView = view.filter_name
        val deleteBut: ImageView = view.button_remove_filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder {

        return FiltersViewHolder(
            LayoutInflater.from(context).inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FiltersViewHolder, position: Int) {

        val name = when{

            items[position].type == FilterType.TYPE && items[position].value == "UNDERGROUND" ->
                context.resources.getString(R.string.underground)

            items[position].type == FilterType.TYPE && items[position].value == "SURFACE" ->
                context.resources.getString(R.string.surface)

            items[position].type == FilterType.AVAILABILITY && items[position].value == "AVAILABLE" ->
                context.resources.getString(R.string.available)

            items[position].type == FilterType.AVAILABILITY && items[position].value == "UNAVAILABLE" ->
                context.resources.getString(R.string.unavailable)

            items[position].type == FilterType.DISTANCE && items[position].value == "CLOSEST" ->
                context.resources.getString(R.string.closest)

            items[position].type == FilterType.DISTANCE && items[position].value == "FURTHEST" ->
                context.resources.getString(R.string.furthest)

            items[position].type == FilterType.FAIR && items[position].value == "GREEN" ->
                context.resources.getString(R.string.fair_green)

            items[position].type == FilterType.FAIR && items[position].value == "YELLOW" ->
                context.resources.getString(R.string.fair_yellow)

            items[position].type == FilterType.FAIR && items[position].value == "RED" ->
                context.resources.getString(R.string.fair_red)

            items[position].type == FilterType.ALPHABETICAL ->
                context.resources.getString(R.string.alphabetically)

            items[position].type == FilterType.FAVORITE ->
                context.resources.getString(R.string.favorites)

            else -> items[position].value
        }

        holder.filterName.text = name
        holder.deleteBut.setOnClickListener {  listener.onClickEvent(items[position]) }
    }
}