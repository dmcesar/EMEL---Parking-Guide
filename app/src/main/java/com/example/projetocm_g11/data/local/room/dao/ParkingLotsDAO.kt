package com.example.projetocm_g11.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projetocm_g11.data.local.entities.ParkingLot

@Dao
interface ParkingLotsDAO {

    @Insert
    suspend fun insert(parkingLot: ParkingLot)

    @Query("SELECT * FROM parking_lots")
    suspend fun getAll(): List<ParkingLot>

    @Query("UPDATE parking_lots SET isFavourite = :value WHERE id = :id")
    suspend fun update(id: String, value: Boolean)
}