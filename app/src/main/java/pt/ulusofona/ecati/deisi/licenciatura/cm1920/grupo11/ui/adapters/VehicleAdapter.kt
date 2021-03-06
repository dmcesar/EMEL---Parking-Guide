package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.adapters

import android.animation.ObjectAnimator
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
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.R
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

        val brand = context.resources.getString(R.string.enter_brand) + ": " + items[position].brand
        val model = context.resources.getString(R.string.enter_model) + ": " + items[position].model
        val plate = context.resources.getString(R.string.enter_plate) + ": " + items[position].plate
        val date = context.resources.getString(R.string.enter_plate_date) + ": " + items[position].getDate()

        holder.brand.text = brand
        holder.model.text = model
        holder.licencePlate.text = plate
        holder.plateDate.text = date

        holder.itemView.setOnClickListener { listener.onClickEvent(items[position]) }

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {

            var onTouchX = 0f
            var swipedDistance = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    /* Save pressed X coordinate */
                    MotionEvent.ACTION_DOWN -> {

                        onTouchX = event.x
                    }

                    /* Calculate swiped distance */
                    MotionEvent.ACTION_MOVE -> {

                        swipedDistance = event.x - onTouchX

                        /* Check if swipe distance is valid */
                        if (swipedDistance < 1000 && swipedDistance > -1000) {

                            ObjectAnimator.ofFloat(
                                holder.itemView,
                                "translationX",
                                swipedDistance
                            ).apply {
                                start()
                            }

                            /* Check swipe direction */
                            if (swipedDistance >= 500) {

                                listener.onSwipeRightEvent(items[position].uuid)

                            } else if (swipedDistance <= -500) {

                                listener.onSwipeLeftEvent(items[position])
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {

                        /* Reset animation */
                        ObjectAnimator.ofFloat(
                            holder.itemView,
                            "translationX",
                            0f
                        ).apply {
                            start()
                        }

                        if (swipedDistance <= 20 && swipedDistance >= -20) {

                            v?.performClick()
                        }
                    }

                    MotionEvent.ACTION_CANCEL -> {

                        /* Reset animation when action is canceled */
                        ObjectAnimator.ofFloat(
                            holder.itemView,
                            "translationX",
                            0f
                        ).apply {
                            start()
                        }
                    }

                    else -> return true
                }

                return true
            }
        })
    }
}