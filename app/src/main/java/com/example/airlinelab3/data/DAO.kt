package com.example.airlinelab3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.airlinelab3.model.Airplane
import kotlinx.coroutines.flow.Flow

@Dao

interface AirplaneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(airplane: Airplane)

    @Update
    suspend fun update(airplane: Airplane)

    @Delete
    suspend fun delete(airplane: Airplane)

    @Query("SELECT * FROM airplanes ORDER BY id DESC")
    fun getAllAirplanes(): Flow<List<Airplane>>

}