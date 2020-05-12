package com.example.projetocm_g11.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projetocm_g11.data.local.entities.ParkingLot
import java.util.*

@Dao
interface ParkingLotsDAO {

    /* Returns nº parking lots with id == value */
    @Query("SELECT COUNT(*) FROM parking_lots WHERE id = :value")
    suspend fun getCountWithID(value: String): Int

    @Insert
    suspend fun insert(parkingLot: ParkingLot)
    
    @Query("UPDATE parking_lots SET active = :active, lastUpdatedAt = :lastUpdatedAt, occupancy = :occupancy WHERE id = :id")
    suspend fun updateData(id: String, active: Int, lastUpdatedAt: Date, occupancy: Int)

    @Query("SELECT * FROM parking_lots WHERE id = :id")
    suspend fun get(id: String): ParkingLot

    @Query("SELECT * FROM parking_lots")
    suspend fun getAll(): List<ParkingLot>

    @Query("UPDATE parking_lots SET isFavourite = :value WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, value: Boolean)
}