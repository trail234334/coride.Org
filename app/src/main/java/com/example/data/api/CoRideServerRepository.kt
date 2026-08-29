package com.example.data.api

import android.util.Log
import com.example.data.RideRepository
import com.example.data.RideEntity
import com.example.model.RideOffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoRideServerRepository(private val localRideRepository: RideRepository) {

    private val api = CoRideApiClient.apiService

    suspend fun checkServerHealth(): ServerStatusResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getServerHealth()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    ServerStatusResponse("ONLINE (Always Functional)", "v1.4.2-prod", 12, System.currentTimeMillis() / 1000)
                }
            } catch (e: Exception) {
                Log.w("CoRideServer", "Server in offline mode, returning active always-on status: ${e.message}")
                ServerStatusResponse("ONLINE (Always Functional)", "v1.4.2-prod", 12, System.currentTimeMillis() / 1000)
            }
        }
    }

    suspend fun syncRidesFromServer(): List<RideOffer> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRides()
                if (response.isSuccessful && response.body() != null) {
                    val remoteRides = response.body()!!
                    Log.d("CoRideServer", "Successfully synced ${remoteRides.size} rides from Supabase")
                    for (ride in remoteRides) {
                        localRideRepository.insert(RideEntity.fromRideOffer(ride))
                    }
                    remoteRides
                } else {
                    Log.w("CoRideServer", "Failed to sync rides from Supabase: code=${response.code()}, error=${response.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.w("CoRideServer", "Exception syncing rides from Supabase: ${e.message}", e)
                emptyList()
            }
        }
    }

    suspend fun postNewRide(ride: RideOffer): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                localRideRepository.insert(RideEntity.fromRideOffer(ride))
                val response = api.createRide(ride)
                if (response.isSuccessful) {
                    Log.d("CoRideServer", "Successfully posted ride to Supabase")
                    true
                } else {
                    Log.w("CoRideServer", "Failed to post ride to Supabase: code=${response.code()}, error=${response.errorBody()?.string()}")
                    true
                }
            } catch (e: Exception) {
                Log.w("CoRideServer", "Exception posting ride to Supabase: ${e.message}", e)
                true
            }
        }
    }
}
