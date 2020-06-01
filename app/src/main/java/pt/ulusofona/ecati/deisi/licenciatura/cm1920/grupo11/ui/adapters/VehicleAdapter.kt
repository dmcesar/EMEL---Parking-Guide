package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnClickListener
import kotlinx.android.synthetic.main.vehicles_list_item.view.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnTouchListener

class VehicleAdapter(private val listener: OnTouchListener, private val context: Context, private val layout: Int, private val items: MutableList<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.VehiclesViewHolder>() {

    class VehiclesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

                                listener.onSwipeRightEvent(items[position].uuid)
                            }
                            onTouchX - 10 > event.x -> {

                                listener.onSwipeLeftEvent(items[position])
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