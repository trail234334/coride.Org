package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import com.example.ui.CoRideUiState
import com.example.ui.theme.*

@Composable
fun BiddingScreen(
    uiState: CoRideUiState,
    onBidAction: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .testTag("bidding_screen")
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
                Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Fare Bidding Center", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Live fuel split offers & driver counter-bids", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = MintGreenSafety)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Bidding Window Active", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Text("02:54 left", color = MintGreenSafety, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.activeBids) { bid ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bid_item_${bid.id}"),
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
                                        .size(40.dp)
                                        .background(LavenderSurface, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(bid.passengerName.take(2), fontWeight = FontWeight.Bold, color = VioletPrimary)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(bid.passengerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CharcoalText)
                                    Text("${bid.passengerCollege} • ⭐ ${bid.passengerRating}", fontSize = 11.sp, color = MutedGrayText)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${String.format("%.2f", bid.bidAmount)}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = VioletPrimary)
                                Text("Asking ₹${String.format("%.2f", bid.askingPrice)}", fontSize = 11.sp, color = MutedGrayText)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (bid.status) {
                                    "ACCEPTED" -> MintGreenSafety.copy(alpha = 0.2f)
                                    "COUNTERED" -> Color(0xFFFEF3C7)
                                    "REJECTED" -> Color(0xFFFEE2E2)
                                    else -> LavenderSurface
                                }
                            ) {
                                Text(
                                    text = "STATUS: ${bid.status}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (bid.status) {
                                        "ACCEPTED" -> MintGreenSafety
                                        "COUNTERED" -> Color(0xFFD97706)
                                        "REJECTED" -> Color(0xFFDC2626)
                                        else -> VioletPrimary
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Text(bid.timestamp, fontSize = 11.sp, color = MutedGrayText)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onBidAction(bid.id, "ACCEPTED") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MintGreenSafety)
                            ) {
                                Text("Accept Bid", color = DarkBackground, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onBidAction(bid.id, "COUNTERED") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Counter")
                            }

                            TextButton(
                                onClick = { onBidAction(bid.id, "REJECTED") }
                            ) {
                                Text("Reject", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
