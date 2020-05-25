package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

class Extensions {

    companion object {

        /* Converts Location to LatLng */
        fun toLatLng(location: Location): LatLng {

            return LatLng(location.latitude, location.longitude)
        }

        /* Converts asset Vector Image to BitMapDescriptor */
        fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
            return ContextCompat.getDrawable(context, vectorResId)?.run {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                BitmapDescriptorFactory.fromBitmap(bitmap)
            }
        }

        fun toLocation(latitude: Double, longitude: Double): Location {

            val location = Location("")

            location.latitude = latitude
            location.longitude = longitude

            return location
        }

        fun calculateDistanceBetween(a: Location, b: Location): Float {

            return a.distanceTo(b)
        }
    }
}