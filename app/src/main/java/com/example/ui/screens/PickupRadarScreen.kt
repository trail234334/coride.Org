package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CoRideUiState
import com.example.ui.components.CoRideRadarCanvas
import com.example.ui.theme.*

@Composable
fun PickupRadarScreen(
    uiState: CoRideUiState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("pickup_radar_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MintGreenSafety, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = DarkBackground)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Find My Pickup Person", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("AirTag-style live radar view", fontSize = 12.sp, color = LavenderAccent)
                }
            }

            IconButton(onClick = { onNavigateBack() }) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AirTag Radar Viewport
        CoRideRadarCanvas(
            radarState = uiState.radarState,
            personName = "Jordan Vance (Driver)"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Pickup Spot Instructions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = MintGreenSafety)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Designated Pickup Spot", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Gate B - Main Oval Campus Circle", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = LavenderAccent)
                Text("Jordan's Honda Civic (7STF890) hazard lights flashing", fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}
