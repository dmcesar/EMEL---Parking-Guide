package pt.ulusofona.ecati.deisi.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.ecati.deisi.data.local.entities.Filter
import pt.ulusofona.ecati.deisi.ui.listeners.OnClickListener
import kotlinx.android.synthetic.main.filters_list_item_portrait.view.*
import pt.ulusofona.ecati.deisi.R

class FiltersAdapter(private val listener: OnClickListener, private val context: Context, private val layout: Int, private val items: MutableList<Filter>) :
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

        val name = when (items[position].value) {

            "UNDERGROUND" ->
                context.resources.getString(R.string.underground)

            "SURFACE" ->
                context.resources.getString(R.string.surface)

            "AVAILABLE" ->
                context.resources.getString(R.string.available)

            "UNAVAILABLE" ->
                context.resources.getString(R.string.unavailable)

            "CLOSEST" ->
                context.resources.getString(R.string.closest)

            "FURTHEST" ->
                context.resources.getString(R.string.furthest)

            "ALPHABETICAL" ->
                context.resources.getString(R.string.alphabetically)

            "FAVORITE" ->
                context.resources.getString(R.string.favorites)

            else -> items[position].value
        }

        holder.filterName.text = name
        holder.deleteBut.setOnClickListener {  listener.onClickEvent(items[position]) }
    }
}