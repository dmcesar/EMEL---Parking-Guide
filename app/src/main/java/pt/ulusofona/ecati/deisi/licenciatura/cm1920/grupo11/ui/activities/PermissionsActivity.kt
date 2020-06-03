package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

abstract class PermissionsActivity(private val requestCode: Int) : AppCompatActivity() {

    fun onRequestPermissions(permissions: Array<String>) {

        var permissionsGiven = 0

        permissions.forEach {

            if(checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                permissionsGiven++
            }
        }

        if(permissionsGiven == permissions.size) {

            onRequestPermissionsSuccess()

        } else requestPermissions(permissions, this.requestCode)
    }

    abstract fun onRequestPermissionsSuccess()

    abstract fun onRequestPermissionsFailure()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == this.requestCode) {

            if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                onRequestPermissionsSuccess()

            } else onRequestPermissionsFailure()
        }
    }
}