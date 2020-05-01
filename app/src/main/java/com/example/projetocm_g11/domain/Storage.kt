package com.example.projetocm_g11.domain

import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Vehicle

class Storage {

    private lateinit var vehicles: HashMap<String, Vehicle>

    private lateinit var parkingLots: HashMap<String, ParkingLot>

    private val filters = ArrayList<Filter>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if (instance == null) {

                    instance = Storage()
                }

                return instance as Storage
            }
        }
    }

    /* Vehicles CRUD and init */

    fun initVehicles(list: ArrayList<Vehicle>) {

        this.vehicles = list.associateBy({it.uuid}, {it}) as HashMap<String, Vehicle>
    }

    fun createVehicle(vehicle: Vehicle) {

        this.vehicles[vehicle.uuid] = vehicle
    }

    fun readVehicles(): List<Vehicle> {

        return this.vehicles.values.toList()
    }

    fun updateVehicle(vehicle: Vehicle) {

        this.vehicles[vehicle.uuid] = vehicle
    }

    fun deleteVehicle(uuid: String) {

        this.vehicles.remove(uuid)
    }

    /* Parking lots operations and init */

    fun initParkingLots(list: ArrayList<ParkingLot>) {

        this.parkingLots = list.associateBy({it.id}, {it}) as HashMap<String, ParkingLot>
    }

    fun readParkingLots(): List<ParkingLot> {

        return this.parkingLots.values.toList()
    }

    fun toggleFavorite(id: String) {

        this.parkingLots[id]?.isFavourite = !this.parkingLots[id]?.isFavourite!!
    }

    /* Filters operations */

    fun readFilters(): List<Filter> {

        return this.filters
    }

    fun addFilter(filter: Filter) {

        this.filters.add(filter)
    }

    fun removeFilter(filter: Filter) {

        for(f in this.filters) {

            if(f == filter) {

                this.filters.remove(f)
                return
            }
        }
    }
}