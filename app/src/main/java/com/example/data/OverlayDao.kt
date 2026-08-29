package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OverlayDao {
    @Query("SELECT * FROM custom_map_overlays ORDER BY timestamp DESC")
    fun getAllOverlays(): Flow<List<OverlayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOverlay(overlay: OverlayEntity)

    @Query("DELETE FROM custom_map_overlays WHERE id = :id")
    suspend fun deleteOverlayById(id: String)

    @Query("UPDATE custom_map_overlays SET isVisible = :isVisible WHERE id = :id")
    suspend fun updateVisibility(id: String, isVisible: Boolean)

    @Query("DELETE FROM custom_map_overlays")
    suspend fun deleteAllOverlays()
}
