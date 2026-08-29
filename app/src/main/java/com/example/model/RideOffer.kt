package com.example.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RideOffer(
    @Json(name = "id") val id: String,
    @Json(name = "driver_name") val driverName: String,
    @Json(name = "driver_college") val driverCollege: String = "CET College",
    @Json(name = "driver_rating") val driverRating: Double = 4.95,
    @Json(name = "is_driver_verified") val isDriverVerified: Boolean = true,
    @Json(name = "vehicle_model") val vehicleModel: String = "Maruti Swift",
    @Json(name = "vehicle_plate") val vehiclePlate: String = "KL-01-CB-4091",
    @Json(name = "origin_name") val originName: String,
    @Json(name = "destination_name") val destinationName: String,
    @Json(name = "origin_lat") val originLat: Double = 8.5475,
    @Json(name = "origin_lng") val originLng: Double = 76.9063,
    @Json(name = "dest_lat") val destLat: Double = 8.4870,
    @Json(name = "dest_lng") val destLng: Double = 76.9528,
    @Json(name = "distance_km") val distanceKm: Double = 18.4,
    @Json(name = "total_seats") val totalSeats: Int = 4,
    @Json(name = "available_seats") var availableSeats: Int = 3,
    @Json(name = "base_price_per_seat") val basePricePerSeat: Double,
    @Json(name = "departure_time") val departureTime: String,
    @Json(name = "is_bidding_open") val isBiddingOpen: Boolean = true,
    @Json(name = "status") var status: String = "UPCOMING",
    @Json(name = "route_deviation_percent") val routeDeviationPercent: Double = 2.4
)
