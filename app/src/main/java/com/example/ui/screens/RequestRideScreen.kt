package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RideOffer
import com.example.ui.CoRideUiState
import com.example.ui.theme.*

@Composable
fun RequestRideScreen(
    uiState: CoRideUiState,
    onPlaceBid: (String, Double) -> Unit,
    onBookRide: (String) -> Unit,
    onDeleteRide: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedRideForBid by remember { mutableStateOf<RideOffer?>(null) }
    var customBidInput by remember { mutableStateOf("70.00") }
    var showBidDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .testTag("request_ride_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LavenderSurface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = VioletPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Find Campus Rides", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Search drivers & place fuel bids", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search ride...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VioletPrimary) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = VioletPrimary,
                unfocusedBorderColor = BorderOutline
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Fare & Fuel Split Calculator
        var showCalcCard by remember { mutableStateOf(false) }
        var calcDistance by remember { mutableStateOf("18.4") }
        var calcPassengers by remember { mutableStateOf("3") }
        var calcFuelRate by remember { mutableStateOf("105.50") }
        var calcKmLiter by remember { mutableStateOf("22.5") }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showCalcCard = !showCalcCard },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = VioletPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Trip Fare Split Calculator", fontWeight = FontWeight.Bold, color = VioletPrimary, fontSize = 14.sp)
                    }
                    Icon(
                        if (showCalcCard) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = VioletPrimary
                    )
                }

                if (showCalcCard) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = calcDistance,
                            onValueChange = { calcDistance = it },
                            label = { Text("Km") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                        OutlinedTextField(
                            value = calcKmLiter,
                            onValueChange = { calcKmLiter = it },
                            label = { Text("km/L") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                        OutlinedTextField(
                            value = calcPassengers,
                            onValueChange = { calcPassengers = it },
                            label = { Text("People") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val dist = calcDistance.toDoubleOrNull() ?: 18.4
                    val kmL = calcKmLiter.toDoubleOrNull() ?: 22.5
                    val rate = calcFuelRate.toDoubleOrNull() ?: 105.50
                    val people = calcPassengers.toIntOrNull() ?: 4
                    val totalFuel = if (kmL > 0) (dist / kmL) * rate else 0.0
                    val farePerSeat = if (people > 0) totalFuel / people else 0.0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Est. Fuel: ₹${String.format("%.2f", totalFuel)}", fontSize = 11.sp, color = CharcoalText)
                            Text("Fair Split / Seat: ₹${String.format("%.2f", farePerSeat)}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = VioletPrimary)
                        }
                        Button(
                            onClick = {
                                customBidInput = String.format("%.2f", farePerSeat)
                                showCalcCard = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Set as Bid Target", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Featured Student Driver Ride", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CharcoalText)
            Text("Room DB Connected", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MintGreenSafety)
        }

        Spacer(modifier = Modifier.height(10.dp))

        val ride = uiState.rideOffers.firstOrNull() ?: RideOffer(
            "ride_default",
            "Alex Morgan",
            "CSE • CET",
            4.98,
            true,
            "Swift Dzire",
            "KL-01-CB-4091",
            "CET Main Gate, Sreekaryam",
            "Thampanoor Bus Terminal, TVM",
            8.5475, 76.9063,
            8.4870, 76.9528,
            18.4,
            4,
            3,
            65.0,
            "03:30 PM",
            true,
            "UPCOMING"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ride_offer_card_${ride.id}"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(LavenderSurface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(ride.driverName.take(2), fontWeight = FontWeight.Bold, color = VioletPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(ride.driverName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CharcoalText)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintGreenSafety, modifier = Modifier.size(14.dp))
                            }
                            Text("${ride.driverCollege} • ⭐ ${ride.driverRating}", fontSize = 12.sp, color = MutedGrayText)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("₹${String.format("%.2f", ride.basePricePerSeat)}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = VioletPrimary)
                            Text("per seat", fontSize = 10.sp, color = MutedGrayText)
                        }
                        if (onDeleteRide != null) {
                            IconButton(
                                onClick = { onDeleteRide(ride.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "Delete ride",
                                    tint = Color.Red.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderOutline)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${ride.originName} ➔ ${ride.destinationName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalText)
                    Text("${ride.availableSeats} seats left", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MintGreenSafety)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            selectedRideForBid = ride
                            customBidInput = String.format("%.2f", ride.basePricePerSeat - 1.0)
                            showBidDialog = true
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Place Fare Bid", fontSize = 12.sp)
                    }

                    val isBooked = uiState.bookedRideIds.contains(ride.id)
                    Button(
                        onClick = {
                            if (!isBooked) {
                                onBookRide(ride.id)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBooked) MintGreenSafety else VioletPrimary
                        )
                    ) {
                        Icon(
                            imageVector = if (isBooked) Icons.Default.Check else Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBooked) "Seat Booked ✓" else "Book Seat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Fare Bidding Modal Dialog
    if (showBidDialog && selectedRideForBid != null) {
        AlertDialog(
            onDismissRequest = { showBidDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gavel, contentDescription = null, tint = VioletPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Place Custom Fare Bid", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text("Asking price: ₹${String.format("%.2f", selectedRideForBid!!.basePricePerSeat)} / seat")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customBidInput,
                        onValueChange = { customBidInput = it },
                        label = { Text("Your Bid Amount (₹)") },
                        leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = VioletPrimary) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = customBidInput.toDoubleOrNull() ?: 7.50
                        onPlaceBid(selectedRideForBid!!.id, amount)
                        showBidDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                ) {
                    Text("Submit Bid")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBidDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
