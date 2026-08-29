package com.example.data.api

import com.example.model.RideOffer
import com.example.model.FareBid
import com.example.model.ChatMessage
import retrofit2.Response
import retrofit2.http.*

data class ServerStatusResponse(
    val status: String,
    val serverVersion: String,
    val activeNodes: Int,
    val uptimeSeconds: Long
)

data class AuthRequest(
    val studentId: String,
    val fullName: String,
    val college: String,
    val department: String
)

data class AuthResponse(
    val success: String,
    val token: String,
    val userId: String,
    val message: String
)

interface CoRideApiService {

    @GET("api/v1/health")
    suspend fun getServerHealth(): Response<ServerStatusResponse>

    @POST("api/v1/auth/verify")
    suspend fun verifyStudentId(@Body request: AuthRequest): Response<AuthResponse>

    @GET("rest/v1/co_ride")
    suspend fun getRides(@Query("select") select: String = "*"): Response<List<RideOffer>>

    @POST("rest/v1/co_ride")
    suspend fun createRide(@Body ride: RideOffer): Response<List<RideOffer>>

    @GET("api/v1/bids")
    suspend fun getBids(@Query("rideId") rideId: String? = null): Response<List<FareBid>>

    @POST("api/v1/bids")
    suspend fun submitBid(@Body bid: FareBid): Response<FareBid>

    @PUT("api/v1/bids/{bidId}/status")
    suspend fun updateBidStatus(
        @Path("bidId") bidId: String,
        @Query("status") status: String
    ): Response<FareBid>

    @GET("api/v1/chats")
    suspend fun getChatMessages(@Query("rideId") rideId: String): Response<List<ChatMessage>>

    @POST("api/v1/chats")
    suspend fun sendChatMessage(@Body message: ChatMessage): Response<ChatMessage>
}
