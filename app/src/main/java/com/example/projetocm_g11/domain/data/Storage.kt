package com.example.projetocm_g11.domain.data

class Storage {

    private val storage = mutableListOf<Vehicle>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if(instance == null) {

                    instance = Storage()
                }

                return instance as Storage
            }
        }
    }

    fun insertVehicle(vehicle: Vehicle) {

        storage.add(vehicle)
    }

    fun deleteVehicle(plate: String) {

        for(v in storage) {

            if(v.plate == plate) {

                storage.remove(v)
                return
            }
        }
    }

    fun updateVehicle(vehicle: Vehicle) {

        for(v in storage) {

            if(v.plate == vehicle.plate) {

                storage.remove(v)
                storage.add(vehicle)
                return
            }
        }
    }

    fun getVehicle(plate: String): Vehicle? {

        for(v in storage) {

            if (v.plate == plate) {

                return v
            }
        }
        return null
    }

    fun getAll(): List<Vehicle> {

        return storage.toList()
    }
}