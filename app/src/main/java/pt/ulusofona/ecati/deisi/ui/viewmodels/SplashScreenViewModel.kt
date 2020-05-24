package pt.ulusofona.ecati.deisi.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pt.ulusofona.ecati.deisi.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.data.local.room.LocalDatabase
import pt.ulusofona.ecati.deisi.data.remote.RetrofitBuilder
import pt.ulusofona.ecati.deisi.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.domain.repository.RepositoryLogic
import pt.ulusofona.ecati.deisi.ui.listeners.OnDataReceivedWithOriginListener

class SplashScreenViewModel(application: Application) : AndroidViewModel(application),
    OnDataReceivedWithOriginListener {

    /* Retrieves local database instance */
    private val localDatabase = LocalDatabase.getInstance(application).parkingLotsDAO()

    /* Domain component */
    private val logic =
        RepositoryLogic(
            ParkingLotsRepository(
                localDatabase,
                RetrofitBuilder.getInstance(
                    ENDPOINT
                )
            )
        )

    private var listener: OnDataReceivedWithOriginListener? = null

    fun requestDataFromRemote() {

        this.logic.getFromRemote()
    }

    fun requestDataFromLocal() {

        this.logic.getFromLocal()
    }

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener
        this.logic.registerListener(this)
    }

    fun unregisterListener() {

        this.listener = null
        this.logic.unregisterListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        this.listener?.onDataReceivedWithOrigin(data, updated)
    }
}