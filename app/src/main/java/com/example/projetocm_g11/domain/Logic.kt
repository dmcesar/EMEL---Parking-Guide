package com.example.projetocm_g11.domain

import com.example.projetocm_g11.domain.data.*
import com.example.projetocm_g11.interfaces.OnDataReceived
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class Logic {

    companion object {

        private var instance: Logic? = null

        fun getInstance(): Logic {

            synchronized(this) {

                if (instance == null) {

                    instance = Logic()
                }

                return instance as Logic
            }
        }
    }

    private val storage = Storage.getInstance()

    /* Observer / Observable data and operations */

    private lateinit var parkingLots: ArrayList<ParkingLot>
    private lateinit var vehicles: ArrayList<Vehicle>
    private var filters: ArrayList<Filter>? = null

    private var parkingLotsListener: OnDataReceived? = null
    private var vehiclesListener: OnDataReceived? = null
    private var filtersListener: OnDataReceived? = null

    fun registerParkingLotsListener(listener: OnDataReceived) {

        this.parkingLotsListener = listener
    }

    fun unregisterParkingLotsListener() {

        this.parkingLotsListener = null
    }

    fun registerVehiclesListener(listener: OnDataReceived) {

        this.vehiclesListener = listener
    }

    fun unregisterVehiclesListener() {

        this.vehiclesListener = null
    }

    fun registerFiltersListener(listener: OnDataReceived) {

        this.filtersListener = listener
    }

    fun unregisterFiltersListener() {

        this.filtersListener = null
    }

    /* Parking lots operations */

    private fun initParkingLotsList() {

        CoroutineScope(Dispatchers.Default).launch {

            val list = ArrayList<ParkingLot>()

            val p1 = ParkingLot("P001", "Campo Grande", true, 1, 200, 200, Date(), 30.12345, 70.54321, Type.SURFACE)

            val p2 = ParkingLot("P002", "Campo Pequeno", false, 2, 500, 300, Date(), 40.12345, 65.54321, Type.UNDERGROUND)

            val p3 = ParkingLot("P003", "Baia de Cascais", true, 3, 70, 10, Date(), 20.12345, -20.54321, Type.UNDERGROUND)

            val p4 = ParkingLot("P004", "CascaisShopping", false, 4, 1000, 910, Date(), 15.12345, 7.54321, Type.UNDERGROUND)

            list.add(p1)
            list.add(p2)
            list.add(p3)
            list.add(p4)

            withContext(Dispatchers.IO) {

                storage.initParkingLots(list)
            }

            withContext(Dispatchers.Main) {

                getParkingLots()
            }
        }
    }

    fun getParkingLots() {

        CoroutineScope(Dispatchers.IO).launch {

            parkingLots = storage.readParkingLots() as ArrayList<ParkingLot>

            withContext(Dispatchers.Main) {

                parkingLotsListener?.onDataReceived(parkingLots)
            }
        }
    }

    fun toggleFavorite(id: String) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.toggleFavorite(id)

            getParkingLots()
        }
    }

    /* Vehicles operations */

    private fun initVehiclesList() {

        CoroutineScope(Dispatchers.Default).launch {

            val list = ArrayList<Vehicle>()

            val v1 = Vehicle("Toyota", "Supra", "12-AB-34")
            val v2 = Vehicle("Peugeot", "508 GT", "56-CD-78")
            val v3 = Vehicle("Mini", "Cooper S", "90-EF-12")
            val v4 = Vehicle("Porsche", "Macan S Turbo", "34-GH-56")
            val v5 = Vehicle("Mazda", "MX-5", "78-IJ-90")

            list.add(v1)
            list.add(v2)
            list.add(v3)
            list.add(v4)
            list.add(v5)

            withContext(Dispatchers.IO) {

                storage.initVehicles(list)
            }
        }
    }

    fun getVehicles() {

        CoroutineScope(Dispatchers.IO).launch {

            vehicles = storage.readVehicles() as ArrayList<Vehicle>

            withContext(Dispatchers.Main) {

                vehiclesListener?.onDataReceived(vehicles)
            }
        }
    }

    fun addVehicle(vehicle: Vehicle) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.createVehicle(vehicle)

            getVehicles()
        }
    }

    fun updateVehicle(vehicle: Vehicle) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.updateVehicle(vehicle)

            getVehicles()
        }
    }

    fun removeVehicle(id: String) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.deleteVehicle(id)

            getVehicles()
        }
    }

    /* Filters operations */

    fun getFilters() {

        CoroutineScope(Dispatchers.IO).launch {

            filters = storage.readFilters() as ArrayList<Filter>

            withContext(Dispatchers.Main) {

                filtersListener?.onDataReceived(filters)
            }
        }
    }

    fun applyFilters() {

        CoroutineScope(Dispatchers.Default).launch {

            var sequence = parkingLots.asSequence()

            filters?.let {

                if(it.size != 0) {

                    for(f in it) {

                        when (f.type) {

                            FilterType.TYPE -> {

                                sequence = if(f.value == "SURFACE") {

                                    sequence.filter { p -> p.type == Type.SURFACE }

                                } else sequence.filter { p -> p.type == Type.UNDERGROUND }
                            }

                            FilterType.AVAILABILITY -> {

                                sequence = if(f.value == "AVAILABLE") {

                                    sequence.filter { p -> p.active }
                                } else sequence.filter { p -> !p.active }
                            }

                            FilterType.NAME -> {

                                sequence.filter { p -> p.name.contains(f.value) }
                            }

                            FilterType.FAVORITE -> {

                                sequence.filter { p -> p.isFavourite }
                            }

                            FilterType.ALPHABETICAL -> {

                                sequence.sortedBy { p -> p.name }
                            }
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {

                parkingLotsListener?.onDataReceived(ArrayList(sequence.toList()))
            }
        }
    }

    fun addFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.addFilter(filter)

            getFilters()
        }
    }

    fun removeFilter(filter: Filter) {

        CoroutineScope(Dispatchers.IO).launch {

            storage.removeFilter(filter)

            getFilters()
        }
    }

    init {

        initParkingLotsList()
        initVehiclesList()
    }
}