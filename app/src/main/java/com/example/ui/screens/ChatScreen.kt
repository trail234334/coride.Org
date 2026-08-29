package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RideOffer
import com.example.ui.CoRideUiState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    uiState: CoRideUiState,
    onSendMessage: (String, Boolean) -> Unit,
    onSelectRide: (RideOffer) -> Unit = {},
    onNavigateToRadar: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var chatInputText by remember { mutableStateOf("") }
    var showPartnerDropdown by remember { mutableStateOf(false) }
    var showCallNotice by remember { mutableStateOf<String?>(null) }
    var showAttachmentMenu by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Find accepted ride partner or booked ride if any
    val acceptedBid = uiState.activeBids.find { it.status == "ACCEPTED" }
    val currentRide = uiState.selectedRide
    val hasBookedRide = currentRide != null && uiState.bookedRideIds.contains(currentRide.id)
    
    val partnerName = when {
        hasBookedRide -> currentRide!!.driverName
        acceptedBid != null -> acceptedBid.passengerName
        else -> ""
    }
    val partnerSub = when {
        hasBookedRide -> "${currentRide!!.driverCollege} • Booked Ride Partner"
        acceptedBid != null -> "${acceptedBid.passengerCollege} • Accepted Bid Partner"
        else -> ""
    }

    if (partnerName.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SurfaceBase)
                .padding(24.dp)
                .testTag("chat_screen"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = VioletPrimary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Active Ride Chat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The chat is clear. Book a seat on a ride from 'Request Ride' or have an accepted bid to chat with your ride partner.",
                fontSize = 14.sp,
                color = MutedGrayText,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        return
    }

    // Auto scroll to bottom on new message
    LaunchedEffect(uiState.chatMessages.size) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    val quickReplies = listOf(
        "📍 Share Location Pin",
        "🚘 Waiting at CET Gate!",
        "⏳ 3 mins away",
        "👋 Hello!"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .testTag("chat_screen")
    ) {
        // Purple & Lavender Top Header Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = VioletPrimary,
            shadowElevation = 4.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar with online status dot
                    Box {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(LavenderAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = partnerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                color = VioletPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(MintGreenSafety)
                                .border(1.5.dp, VioletPrimary, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Partner Info & Accepted Ride status
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showPartnerDropdown = !showPartnerDropdown }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = partnerName,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Accepted Partner",
                                tint = MintGreenSafety,
                                modifier = Modifier.size(15.dp)
                            )
                            Icon(
                                imageVector = if (showPartnerDropdown) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = "🔒 Accepted Ride Chat • $partnerSub",
                            color = LavenderAccent.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Action Icons: Phone Call, Video Call
                    IconButton(onClick = { showCallNotice = "Calling $partnerName securely... 📞" }) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                    }
                    IconButton(onClick = { showCallNotice = "Starting secure video call with $partnerName... 📹" }) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White)
                    }
                }

                // Accepted Partner Selection Dropdown
                DropdownMenu(
                    expanded = showPartnerDropdown,
                    onDismissRequest = { showPartnerDropdown = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    Text(
                        "Accepted Ride Partner Chats:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                    HorizontalDivider()
                    uiState.activeBids.filter { it.status == "ACCEPTED" }.forEach { bid ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(bid.passengerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${bid.passengerCollege} • Fare: ₹${bid.bidAmount}", fontSize = 11.sp, color = MutedGrayText)
                                }
                            },
                            onClick = {
                                showPartnerDropdown = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintGreenSafety)
                            }
                        )
                    }
                    if (uiState.activeBids.none { it.status == "ACCEPTED" }) {
                        DropdownMenuItem(
                            text = { Text("Alex Morgan (Driver - Default)", fontSize = 13.sp) },
                            onClick = { showPartnerDropdown = false },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary) }
                        )
                    }
                }
            }
        }

        // Call notification popup toast
        showCallNotice?.let { msg ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = LavenderSurface,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(msg, color = VioletPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { showCallNotice = null }) {
                        Text("DISMISS", color = VioletPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Chat Messages Thread Container (Purple/Lavender Theme)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
            ) {
                item {
                    // Security Notice Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LavenderSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SECURED CHAT • ONLY OPEN WITH ACCEPTED RIDE PARTNER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VioletPrimary
                                )
                            }
                        }
                    }
                }

                items(uiState.chatMessages) { msg ->
                    val isMe = msg.senderName.contains("You") || !msg.isDriver

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isMe) 16.dp else 4.dp,
                                bottomEnd = if (isMe) 4.dp else 16.dp
                            ),
                            color = if (isMe) LavenderSurface else Color.White,
                            shadowElevation = 1.dp,
                            border = if (!isMe) androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent) else null,
                            modifier = Modifier.widthIn(max = 290.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                if (!isMe) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = msg.senderName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VioletPrimary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = MintGreenSafety,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                }

                                Text(
                                    text = msg.text,
                                    fontSize = 14.sp,
                                    color = CharcoalText,
                                    lineHeight = 18.sp
                                )

                                if (msg.isLocationPin) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = LavenderSurface),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNavigateToRadar() }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = VioletPrimary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    "Live GPS Pickup Pin",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = VioletPrimary
                                                )
                                                Text(
                                                    "Tap to open Radar Tracking 📍",
                                                    fontSize = 10.sp,
                                                    color = MutedGrayText
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.align(Alignment.End),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = msg.timestamp,
                                        fontSize = 10.sp,
                                        color = MutedGrayText
                                    )
                                    if (isMe) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.DoneAll,
                                            contentDescription = "Read",
                                            tint = VioletPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Suggestion Chips (Lavender theme)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickReplies.forEach { reply ->
                SuggestionChip(
                    onClick = {
                        val isPin = reply.contains("Pin")
                        onSendMessage(reply, isPin)
                    },
                    label = { Text(reply, fontSize = 11.sp, color = VioletPrimary, fontWeight = FontWeight.SemiBold) },
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = LavenderAccent
                    ),
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = LavenderSurface)
                )
            }
        }

        // Attachment Menu Drawer
        AnimatedVisibility(visible = showAttachmentMenu) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onSendMessage("Shared Live Pickup Location Pin", true)
                            showAttachmentMenu = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(VioletPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = Color.White)
                        }
                        Text("Location Pin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CharcoalText, modifier = Modifier.padding(top = 4.dp))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onSendMessage("Confirmed fare split agreement.", false)
                            showAttachmentMenu = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(VioletPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color.White)
                        }
                        Text("Fare Split", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CharcoalText, modifier = Modifier.padding(top = 4.dp))
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onSendMessage("Shared Verified Student ID Badge.", false)
                            showAttachmentMenu = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(VioletPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = Color.White)
                        }
                        Text("Share ID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CharcoalText, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }

        // Purple & Lavender Bottom Input Dock Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LavenderAccent)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        IconButton(onClick = { showAttachmentMenu = !showAttachmentMenu }) {
                            Icon(
                                imageVector = if (showAttachmentMenu) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = "Attach",
                                tint = VioletPrimary
                            )
                        }

                        OutlinedTextField(
                            value = chatInputText,
                            onValueChange = { chatInputText = it },
                            placeholder = { Text("Message accepted partner...", color = MutedGrayText, fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        IconButton(onClick = { onSendMessage("Location Pin", true) }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = VioletPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Purple Round Send Button
                FloatingActionButton(
                    onClick = {
                        if (chatInputText.isNotBlank()) {
                            onSendMessage(chatInputText, false)
                            chatInputText = ""
                        }
                    },
                    containerColor = VioletPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
