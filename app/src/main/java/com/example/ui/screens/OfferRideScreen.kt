package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.CoRideUiState
import com.example.ui.theme.*

@Composable
fun OfferRideScreen(
    uiState: CoRideUiState,
    onOfferCreated: (String, String, Int, Boolean, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var originInput by remember { mutableStateOf("CET Main Gate, Sreekaryam") }
    var destInput by remember { mutableStateOf("Thampanoor Bus Terminal, TVM") }
    var seatCount by remember { mutableIntStateOf(3) }
    var isBiddingEnabled by remember { mutableStateOf(true) }
    var pricePerSeat by remember { mutableStateOf("75.00") }
    var tripDistanceInput by remember { mutableStateOf("18.4") }
    var fuelPriceInput by remember { mutableStateOf("105.50") }
    var carEfficiencyInput by remember { mutableStateOf(uiState.selectedCarModel.avgKmPerLiter.toString()) }

    val distDouble = tripDistanceInput.toDoubleOrNull() ?: 18.4
    val fuelRateDouble = fuelPriceInput.toDoubleOrNull() ?: 105.50
    val efficiencyDouble = carEfficiencyInput.toDoubleOrNull() ?: 22.5
    val estimatedTotalFuelCost = if (efficiencyDouble > 0) (distDouble / efficiencyDouble) * fuelRateDouble else 0.0
    val totalOccupants = seatCount + 1
    val calculatedFairShare = if (totalOccupants > 0) estimatedTotalFuelCost / totalOccupants else 0.0
    val suggestedFare = Math.max(calculatedFairShare, 35.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("offer_ride_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(VioletPrimary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Offer Campus Ride", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Share seats & split trip fuel cost", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Route Location Inputs
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = originInput,
                    onValueChange = { originInput = it },
                    label = { Text("Pickup Location") },
                    leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = VioletPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderOutline
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = destInput,
                    onValueChange = { destInput = it },
                    label = { Text("Destination Location") },
                    leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null, tint = MintGreenSafety) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderOutline
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fare & Fuel Estimator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = VioletPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Estimated Trip Price Calculator", fontWeight = FontWeight.ExtraBold, color = VioletPrimary, fontSize = 15.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = uiState.selectedCarModel.model,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = VioletPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = tripDistanceInput,
                        onValueChange = { tripDistanceInput = it },
                        label = { Text("Dist (km)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                    )

                    OutlinedTextField(
                        value = carEfficiencyInput,
                        onValueChange = { carEfficiencyInput = it },
                        label = { Text("Mileage (km/L)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                    )

                    OutlinedTextField(
                        value = fuelPriceInput,
                        onValueChange = { fuelPriceInput = it },
                        label = { Text("Rate (₹/L)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Trip Fuel Cost: ₹${String.format("%.2f", estimatedTotalFuelCost)}", fontSize = 12.sp, color = CharcoalText)
                        Text("Fair Share / Seat (${seatCount} passengers + 1 driver): ₹${String.format("%.2f", calculatedFairShare)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletPrimary)
                    }

                    Button(
                        onClick = {
                            pricePerSeat = String.format("%.2f", suggestedFare)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Apply ₹${String.format("%.0f", suggestedFare)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seats & Bidding Configuration
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    Text("Available Seats", fontWeight = FontWeight.Bold, color = CharcoalText)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (seatCount > 1) seatCount-- },
                            enabled = seatCount > 1
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$seatCount seats",
                            fontWeight = FontWeight.ExtraBold,
                            color = VioletPrimary,
                            fontSize = 16.sp
                        )

                        IconButton(
                            onClick = { if (seatCount < 6) seatCount++ }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BorderOutline)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Allow Fare Bidding", fontWeight = FontWeight.Bold, color = CharcoalText)
                        Text("Passengers can offer custom fuel bids", fontSize = 11.sp, color = MutedGrayText)
                    }

                    Switch(
                        checked = isBiddingEnabled,
                        onCheckedChange = { isBiddingEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = VioletPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pricePerSeat,
                    onValueChange = { pricePerSeat = it },
                    label = { Text("Asking Price Per Seat (₹)") },
                    leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = VioletPrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderOutline
                    )
                )
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val price = pricePerSeat.toDoubleOrNull() ?: 75.0
                onOfferCreated(originInput, destInput, seatCount, isBiddingEnabled, price)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("publish_ride_offer_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
        ) {
            Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Publish Ride Offer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
