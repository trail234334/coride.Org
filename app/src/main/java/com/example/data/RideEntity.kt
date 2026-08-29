package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.RideOffer

@Entity(tableName = "saved_rides")
data class RideEntity(
    @PrimaryKey val id: String,
    val driverName: String,
    val driverCollege: String = "CET College",
    val driverRating: Double = 4.95,
    val isDriverVerified: Boolean = true,
    val vehicleModel: String = "Maruti Swift",
    val vehiclePlate: String = "KL-01-CB-4091",
    val originName: String,
    val destinationName: String,
    val originLat: Double = 8.5475,
    val originLng: Double = 76.9063,
    val destLat: Double = 8.4870,
    val destLng: Double = 76.9528,
    val distanceKm: Double = 18.4,
    val totalSeats: Int = 4,
    val availableSeats: Int = 3,
    val price: Double,
    val departureTime: String,
    val isBidding: Boolean = true,
    val status: String = "UPCOMING",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toRideOffer(): RideOffer {
        return RideOffer(
            id, driverName, driverCollege, driverRating, isDriverVerified,
            vehicleModel, vehiclePlate, originName, destinationName,
            originLat, originLng, destLat, destLng, distanceKm,
            totalSeats, availableSeats, price, departureTime, isBidding, status
        )
    }

    companion object {
        fun fromRideOffer(offer: RideOffer): RideEntity {
            return RideEntity(
                id = offer.id,
                driverName = offer.driverName,
                driverCollege = offer.driverCollege,
                driverRating = offer.driverRating,
                isDriverVerified = offer.isDriverVerified,
                vehicleModel = offer.vehicleModel,
                vehiclePlate = offer.vehiclePlate,
                originName = offer.originName,
                destinationName = offer.destinationName,
                originLat = offer.originLat,
                originLng = offer.originLng,
                destLat = offer.destLat,
                destLng = offer.destLng,
                distanceKm = offer.distanceKm,
                totalSeats = offer.totalSeats,
                availableSeats = offer.availableSeats,
                price = offer.basePricePerSeat,
                departureTime = offer.departureTime,
                isBidding = offer.isBiddingOpen,
                status = offer.status
            )
        }
    }
}

