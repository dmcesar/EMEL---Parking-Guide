package com.example.projetocm_g11.domain

import com.example.projetocm_g11.domain.data.Vehicle

class Storage {

    private val storage = HashMap<String, Vehicle>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if(instance == null) {

                    instance =
                        Storage()
                }

                return instance as Storage
            }
        }
    }

    fun create(vehicle: Vehicle) {

        storage[vehicle.uuid] = vehicle
    }

    fun read(): List<Vehicle> {

        return storage.values.toList()
    }

    fun update(vehicle: Vehicle) {

        storage[vehicle.uuid] = vehicle
    }

    fun delete(uuid: String) {

        storage.remove(uuid)
    }
}