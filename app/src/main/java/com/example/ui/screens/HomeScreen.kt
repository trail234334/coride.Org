package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.CoRideUiState
import com.example.ui.components.InteractiveGoogleMapView
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    uiState: CoRideUiState,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    onCheckServerHealth: () -> Unit = {},
    onSyncBackend: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("home_screen_container")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Greeting Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(VioletPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.currentUser.name.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hello, ${uiState.currentUser.name.split(" ").first()}!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Badge",
                            tint = MintGreenSafety,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "${uiState.currentUser.collegeName} • Verified",
                        fontSize = 12.sp,
                        color = VioletPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Surface(
                onClick = { onNavigate("safety") },
                shape = CircleShape,
                color = LavenderSurface,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Safety Center",
                        tint = VioletPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Banner Graphic
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
                .testTag("hero_banner_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_coride_1785768674864),
                    contentDescription = "CoRide Hero Graphic",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(VioletPrimary.copy(alpha = 0.92f), VioletPrimary.copy(alpha = 0.2f))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(220.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Campus Shared Fuel Rides",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Split fuel, cut traffic, connect with peers.",
                            fontSize = 11.sp,
                            color = LavenderSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cloud Backend Status & Sync Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("backend_status_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LavenderSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = VioletPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CoRide Cloud Backend",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalText
                            )
                            val statusText = uiState.serverStatus?.status ?: "ONLINE (Always Functional)"
                            val isOffline = false
                            Text(
                                text = statusText,
                                fontSize = 11.sp,
                                color = MintGreenSafety,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = onCheckServerHealth,
                        modifier = Modifier.size(36.dp)
                    ) {
                        if (uiState.isCheckingServer) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = VioletPrimary)
                        } else {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Check Health", tint = VioletPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                if (uiState.serverStatus != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Version: ${uiState.serverStatus.serverVersion}", fontSize = 11.sp, color = MutedGrayText)
                        Text(text = "Active Nodes: ${uiState.serverStatus.activeNodes}", fontSize = 11.sp, color = MutedGrayText)
                    }
                }

                if (!uiState.backendSyncMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.backendSyncMessage!!,
                        fontSize = 11.sp,
                        color = VioletPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onSyncBackend,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                ) {
                    if (uiState.isSyncingBackend) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Syncing Backend...", fontSize = 12.sp)
                    } else {
                        Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sync Rides & Bids from Backend", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Action Grid
        Text(
            text = "Quick Services",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalText
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickServiceCard(
                title = "Offer Ride",
                subtitle = "Driver Mode",
                icon = Icons.Default.DirectionsCar,
                backgroundColor = VioletPrimary,
                contentColor = Color.White,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("offer") }
            )

            QuickServiceCard(
                title = "Request Ride",
                subtitle = "Passenger Mode",
                icon = Icons.Default.Search,
                backgroundColor = LavenderSurface,
                contentColor = VioletPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("request") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickServiceCard(
                title = "Fuel Calculator",
                subtitle = "Car MPG & Split",
                icon = Icons.Default.LocalGasStation,
                backgroundColor = LavenderSurface,
                contentColor = CharcoalText,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("fuel") }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upcoming Active Ride Card
        Text(
            text = "Active Scheduled Ride",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CharcoalText
        )

        Spacer(modifier = Modifier.height(8.dp))

        uiState.selectedRide?.let { ride ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upcoming_ride_card"),
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
                                Text(
                                    text = ride.driverName.take(2),
                                    fontWeight = FontWeight.Bold,
                                    color = VioletPrimary,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(ride.driverName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CharcoalText)
                                Text("${ride.driverCollege} • ⭐ ${ride.driverRating}", fontSize = 11.sp, color = MutedGrayText)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LavenderSurface
                        ) {
                            Text(
                                text = "₹${String.format("%.2f", ride.basePricePerSeat)} / seat",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VioletPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderOutline)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(ride.departureTime, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalText)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(ride.vehicleModel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalText)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigate("chat") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ride Chat", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onNavigate("bidding") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Bids (${uiState.activeBids.size})", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QuickServiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isPrimary = backgroundColor == VioletPrimary
    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() }
            .testTag("quick_service_$title"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isPrimary) null else androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPrimary) 2.dp else 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(contentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = contentColor.copy(alpha = 0.82f)
                )
            }
        }
    }
}
