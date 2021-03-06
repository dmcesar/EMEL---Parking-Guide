package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.room.dao

import androidx.room.*
import pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.data.local.entities.Vehicle

@Dao
interface VehiclesDAO {

    @Insert
    suspend fun insert(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles")
    suspend fun getAll(): List<Vehicle>

    @Update
    suspend fun update(vehicle: Vehicle)

    @Query("DELETE FROM vehicles WHERE uuid = :id")
    suspend fun delete(id: String)
}