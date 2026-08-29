package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Query("SELECT * FROM saved_rides ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<RideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: RideEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rides: List<RideEntity>)

    @Query("DELETE FROM saved_rides WHERE id = :id")
    suspend fun deleteRideById(id: String)

    @Query("DELETE FROM saved_rides")
    suspend fun deleteAllRides()

    @Query("SELECT COUNT(*) FROM saved_rides")
    suspend fun getRideCount(): Int
}

