package com.example.projetocm_g11.domain

import com.example.projetocm_g11.domain.data.Vehicle

class Storage {

    private val storage = HashMap<String, Vehicle>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if (instance == null) {

                    instance = Storage()

                    initList()
                }

                return instance as Storage
            }
        }

        private fun initList() {

            val v1 = Vehicle("Toyota", "Supra", "12-AB-34")
            val v2 = Vehicle("Peugeot", "508 GT", "56-CD-78")
            val v3 = Vehicle("Porsche", "Taycan", "90-EF-12")

            instance?.create(v1)
            instance?.create(v2)
            instance?.create(v3)
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