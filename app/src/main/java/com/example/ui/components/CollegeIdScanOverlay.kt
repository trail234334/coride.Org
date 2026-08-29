package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CollegeIdScanOverlay(
    isScanning: Boolean = true,
    extractedName: String = "",
    extractedCollege: String = "",
    scanTitle: String = "Align Student ID Card in Frame",
    buttonText: String = "Scan ID Card",
    onScanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LaserScan")
    val laserYRatio by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserY"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkBackground)
            .border(2.dp, VioletLight, RoundedCornerShape(20.dp))
            .testTag("id_card_scan_overlay"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Card Frame Box (85.6mm x 53.95mm standard ID card aspect ratio)
            val cardW = width * 0.82f
            val cardH = height * 0.68f
            val cardTop = (height - cardH) / 2f
            val cardLeft = (width - cardW) / 2f

            // Darken outside frame
            drawRect(color = Color.Black.copy(alpha = 0.45f))

            // Transparent ID viewport cutout
            drawRect(
                color = DarkCard.copy(alpha = 0.85f),
                topLeft = Offset(cardLeft, cardTop),
                size = Size(cardW, cardH)
            )

            // Animated Laser Line
            if (isScanning) {
                val laserY = cardTop + (cardH * laserYRatio)
                drawLine(
                    color = VioletLight,
                    start = Offset(cardLeft + 10f, laserY),
                    end = Offset(cardLeft + cardW - 10f, laserY),
                    strokeWidth = 6f
                )
            }
        }

        // Viewfinder Header
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = "ID Badge",
                tint = MintGreenSafety,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = scanTitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Scan Action Trigger Button
        Button(
            onClick = { onScanClick() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Scan ID",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buttonText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
