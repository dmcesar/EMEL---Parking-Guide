package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.contacts.ContactsLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedListener

class ContactsViewModel(application: Application) : AndroidViewModel(application), OnDataReceivedListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).vehiclesDAO()

    private val logic = ContactsLogic(localDatabase)

    private var listener: OnDataReceivedListener? = null

    fun getAll() {

        this.logic.read()
    }

    fun registerListener(listener: OnDataReceivedListener) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    override fun onDataReceived(data: ArrayList<*>?) {
        this.listener?.onDataReceived(data)
    }
}