package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CoRideUiState
import com.example.ui.theme.*

@Composable
fun SafetyCenterScreen(
    uiState: CoRideUiState,
    onTriggerSos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("safety_center_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MintGreenSafety, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = DarkBackground)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Campus Safety Center", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = CharcoalText)
                Text("Verification, emergency contacts & SOS", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Verified Status Badge Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MintGreenSafety),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = MintGreenSafety, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("100% Student Verified", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = CharcoalText)
                    }

                    Surface(shape = RoundedCornerShape(8.dp), color = MintGreenSafety.copy(alpha = 0.15f)) {
                        Text("ACTIVE", color = MintGreenSafety, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderOutline)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Matched against official university domain: @${uiState.currentUser.email.split("@").last()}", fontSize = 12.sp, color = MutedGrayText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Emergency Contacts
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderOutline),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Trusted Emergency Contacts", fontWeight = FontWeight.Bold, color = CharcoalText)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalPolice, contentDescription = null, tint = VioletPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Stanford Campus Security Patrol", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Auto-Linked", fontSize = 11.sp, color = MintGreenSafety, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mom (Primary Safety Contact)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("SMS Alerts ON", fontSize = 11.sp, color = MintGreenSafety, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Test SOS Button
        Button(
            onClick = { onTriggerSos() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("test_sos_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Trigger Emergency SOS Test Alert", fontWeight = FontWeight.Bold)
        }
    }
}
