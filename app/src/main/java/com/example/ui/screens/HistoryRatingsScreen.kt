package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.*

@Composable
fun HistoryRatingsScreen(
    uiState: CoRideUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .testTag("history_ratings_screen")
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
                Icon(Icons.Default.History, contentDescription = null, tint = VioletPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Ride History & Ratings", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Past trips, peer reviews & fuel savings", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Savings Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VioletPrimary)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Fuel Money Saved", color = LavenderAccent, fontSize = 12.sp)
                    Text("₹3,450.00", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text("34 Rides Completed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Past Campus Trips", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CharcoalText)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(4) { idx ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("CET Campus ➔ Thampanoor Terminal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("-₹75.00", fontWeight = FontWeight.Bold, color = VioletPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Yesterday • Driver: Alex Morgan • ⭐ 5.0 Rated", fontSize = 11.sp, color = MutedGrayText)
                    }
                }
            }
        }
    }
}
