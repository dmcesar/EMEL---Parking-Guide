package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.ParkingLotsDAO
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao.VehiclesDAO
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.ParkingLot
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle

@Database(entities = [ParkingLot::class, Vehicle::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun parkingLotsDAO(): ParkingLotsDAO
    abstract fun vehiclesDAO(): VehiclesDAO

    companion object {

        private var instance: LocalDatabase? = null

        fun getInstance(applicationContext: Context): LocalDatabase {

            synchronized(this) {

                if(instance == null) {

                    instance = Room.databaseBuilder(
                        applicationContext,
                        LocalDatabase::class.java,
                        "emel_parking_db"
                    ).build()
                }

                return instance as LocalDatabase
            }
        }
    }
}