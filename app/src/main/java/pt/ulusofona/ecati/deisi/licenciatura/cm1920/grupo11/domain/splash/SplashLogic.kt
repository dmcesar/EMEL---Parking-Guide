package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.splash

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.repositories.ParkingLotsRepository
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.domain.repository.RepositoryLogic
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners.OnDataReceivedWithOriginListener

class SplashLogic(repository: ParkingLotsRepository) : RepositoryLogic(repository) {

    private var listener: OnDataReceivedWithOriginListener? = null

    fun registerListener(listener: OnDataReceivedWithOriginListener) {

        this.listener = listener

        super.registerListener()
    }

    override fun unregisterListener() {

        this.listener = null
        super.unregisterListener()
    }

    override fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean) {

        this.listener?.onDataReceivedWithOrigin(data, updated)
    }
}