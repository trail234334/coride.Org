package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_map_overlays")
data class OverlayEntity(
    @PrimaryKey val id: String,
    val type: String, // "MARKER", "POLYGON", "TILE_LAYER"
    val title: String,
    val description: String,
    val category: String, // "Pickup Point", "Safe Waiting Zone", "Campus Shuttle Layer", "Custom Polygon"
    val colorHex: String, // e.g. "#005AC1", "#00875A", "#BA1A1A"
    val pointsJson: String, // Lat/Lng coordinates string e.g. "8.5475,76.9063;8.5490,76.9080"
    val iconType: String, // "PIN", "SHIELD", "BUS", "PARKING", "HEATMAP"
    val isVisible: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
