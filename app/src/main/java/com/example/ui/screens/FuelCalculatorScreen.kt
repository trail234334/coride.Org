package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.model.CarModelSpec
import com.example.ui.CoRideUiState
import com.example.ui.components.FuelGaugeCanvas
import com.example.ui.theme.*

@Composable
fun FuelCalculatorScreen(
    uiState: CoRideUiState,
    onCarSearch: (String) -> Unit,
    onCarSelected: (CarModelSpec) -> Unit,
    onInputsChanged: (Double, Double, Double, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isCarDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("fuel_calculator_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
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
                Icon(Icons.Default.LocalGasStation, contentDescription = null, tint = VioletPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Mileage & Fuel Calculator", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Car database MPG + passenger fuel split", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Animated Gauge Canvas
        FuelGaugeCanvas(calculation = uiState.fuelCalculation)

        Spacer(modifier = Modifier.height(18.dp))

        // Car Model Searchable Picker Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Vehicle Model (Auto-fills Mileage)", fontWeight = FontWeight.Bold, color = CharcoalText)

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.selectedCarModel.getFullName(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isCarDropdownExpanded = !isCarDropdownExpanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = VioletPrimary)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VioletPrimary,
                            unfocusedBorderColor = BorderOutline
                        )
                    )

                    DropdownMenu(
                        expanded = isCarDropdownExpanded,
                        onDismissRequest = { isCarDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .heightIn(max = 240.dp)
                    ) {
                        uiState.availableCarsList.forEach { car ->
                            DropdownMenuItem(
                                text = { Text(car.getFullName()) },
                                onClick = {
                                    onCarSelected(car)
                                    isCarDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderOutline)
                Spacer(modifier = Modifier.height(14.dp))

                // Distance Control
                Text("Trip Distance: ${String.format("%.1f", uiState.tripDistanceKm)} km", fontWeight = FontWeight.Bold)
                Slider(
                    value = uiState.tripDistanceKm.toFloat(),
                    onValueChange = {
                        onInputsChanged(it.toDouble(), uiState.customCarKmPerLiter, uiState.fuelPricePerLiter, uiState.passengersCount)
                    },
                    valueRange = 2f..100f,
                    colors = SliderDefaults.colors(thumbColor = VioletPrimary, activeTrackColor = VioletPrimary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Passengers Split Control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Passengers Sharing Fuel", fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (uiState.passengersCount > 1) {
                                    onInputsChanged(uiState.tripDistanceKm, uiState.customCarKmPerLiter, uiState.fuelPricePerLiter, uiState.passengersCount - 1)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }

                        Text("${uiState.passengersCount} passengers", fontWeight = FontWeight.ExtraBold, color = VioletPrimary)

                        IconButton(
                            onClick = {
                                if (uiState.passengersCount < 5) {
                                    onInputsChanged(uiState.tripDistanceKm, uiState.customCarKmPerLiter, uiState.fuelPricePerLiter, uiState.passengersCount + 1)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
