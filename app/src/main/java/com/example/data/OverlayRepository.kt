package com.example.data

import kotlinx.coroutines.flow.Flow

class OverlayRepository(private val overlayDao: OverlayDao) {
    val allOverlays: Flow<List<OverlayEntity>> = overlayDao.getAllOverlays()

    suspend fun insert(overlay: OverlayEntity) = overlayDao.insertOverlay(overlay)
    suspend fun deleteById(id: String) = overlayDao.deleteOverlayById(id)
    suspend fun setVisibility(id: String, visible: Boolean) = overlayDao.updateVisibility(id, visible)
    suspend fun clearAll() = overlayDao.deleteAllOverlays()
}
