package com.example.data

import kotlinx.coroutines.flow.Flow

class RideRepository(private val rideDao: RideDao) {
    val allRides: Flow<List<RideEntity>> = rideDao.getAllRides()

    suspend fun insert(ride: RideEntity) = rideDao.insertRide(ride)
    suspend fun insertAll(rides: List<RideEntity>) = rideDao.insertAll(rides)
    suspend fun deleteById(id: String) = rideDao.deleteRideById(id)
    suspend fun deleteAll() = rideDao.deleteAllRides()
    suspend fun getCount(): Int = rideDao.getRideCount()
}

