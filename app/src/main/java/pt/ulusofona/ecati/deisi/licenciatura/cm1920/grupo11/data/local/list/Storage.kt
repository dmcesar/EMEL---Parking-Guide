package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.list

import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Filter

class Storage {

    private val storage = ArrayList<Filter>()

    companion object {

        private var instance: Storage? = null

        fun getInstance(): Storage {

            synchronized(this) {

                if (instance == null) {

                    instance =
                        Storage()
                }

                return instance as Storage
            }
        }
    }

    fun getAll(): ArrayList<Filter> {

        return storage
    }

    fun insert(filter: Filter) {

        storage.add(filter)
    }

    fun delete(filter: Filter) {

        storage.remove(filter)
    }
}