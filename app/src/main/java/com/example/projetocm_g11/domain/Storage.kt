package com.example.projetocm_g11.domain

import com.example.projetocm_g11.domain.data.Filter
import com.example.projetocm_g11.domain.data.ParkingLot
import com.example.projetocm_g11.domain.data.Type
import com.example.projetocm_g11.domain.data.Vehicle
import java.util.*
import kotlin.collections.ArrayList

class Storage {

    private val parkingLots = ArrayList<ParkingLot>()

    private val vehicles = ArrayList<Vehicle>()

    private val filters = ArrayList<Filter>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if (instance == null) {

                    instance = Storage()

                    initData()
                }

                return instance as Storage
            }
        }

        private fun initData() {

            val v1 = Vehicle("Toyota", "Supra", "12-AB-34")
            val v2 = Vehicle("Peugeot", "508 GT", "56-CD-78")
            val v3 = Vehicle("Porsche", "Taycan", "90-EF-12")

            this.instance?.vehicles?.add(v1)
            this.instance?.vehicles?.add(v2)
            this.instance?.vehicles?.add(v3)

            val p1 = ParkingLot(
                "P001",
                "El Corte Inglés",
                true,
                1,
                800,
                750,
                Date(),
                38.75463622,
                -9.3414141,
                Type.UNDERGROUND
            )

            val p2 = ParkingLot(
                "P002",
                "Campo Grande",
                true,
                2,
                200,
                40,
                Date(),
                76.3245424,
                90.324143,
                Type.SURFACE
            )

            val p3 = ParkingLot(
                "P003",
                "Baia de Cascais",
                false,
                3,
                50,
                50,
                Date(),
                -50.324324,
                -7.214122,
                Type.UNDERGROUND
            )

            val p4 = ParkingLot(
                "P019",
                "CascaisShopping",
                true,
                4,
                1000,
                650,
                Date(),
                -80.24324,
                43.541343,
                Type.UNDERGROUND
            )

            this.instance?.parkingLots?.add(p1)
            this.instance?.parkingLots?.add(p2)
            this.instance?.parkingLots?.add(p3)
            this.instance?.parkingLots?.add(p4)
        }
    }

    /* Parking lots operations */

    fun getParkingLots(): ArrayList<ParkingLot> {

        return this.parkingLots
    }

    fun toggleFavorite(id: String) {

        for(p in this.parkingLots) {

            if(p.id == id) {

                p.isFavourite = !p.isFavourite
                return
            }
        }
    }

    /* Vehicle operations */

    fun addVehicle(vehicle: Vehicle) {

        this.vehicles.add(vehicle)
    }

    fun getVehicles(): ArrayList<Vehicle> {

        return this.vehicles
    }

    fun updateVehicle(vehicle: Vehicle) {

        for(v in this.vehicles) {

            if(v.uuid == vehicle.uuid) {

                this.vehicles.remove(v)
                this.vehicles.add(vehicle)
                return
            }
        }
    }

    fun removeVehicle(uuid: String) {

        for(v in this.vehicles) {

            if(v.uuid == uuid) {

                this.vehicles.remove(v)
                return
            }
        }
    }

    /* Filter operations */

    fun getFilters(): ArrayList<Filter> {

        return this.filters
    }

    fun addFilter(filter: Filter) {

        if(!filters.contains(filter)) {

            this.filters.add(filter)
        }
    }

    fun removeFilter(filter: Filter) {

        if(filters.contains(filter)) {

            this.filters.remove(filter)
        }
    }
}