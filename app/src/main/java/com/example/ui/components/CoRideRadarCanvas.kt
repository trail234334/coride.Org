package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.RadarTrackingService.RadarPositionState
import com.example.ui.theme.*

@Composable
fun CoRideRadarCanvas(
    radarState: RadarPositionState,
    personName: String = "Jordan (Driver)",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")

    val pulseRadiusFactor by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseRadius"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(DarkBackground)
            .border(2.dp, VioletLight.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
            .testTag("radar_canvas_box"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.width.coerceAtMost(size.height) * 0.42f

            // Concentric Radar Rings
            for (i in 1..4) {
                drawCircle(
                    color = VioletLight.copy(alpha = 0.25f),
                    radius = maxRadius * (i / 4f),
                    center = center,
                    style = Stroke(width = 2f)
                )
            }

            // Pulsing Radar Ring
            drawCircle(
                color = MintGreenSafety.copy(alpha = pulseAlpha),
                radius = maxRadius * pulseRadiusFactor,
                center = center,
                style = Stroke(width = 4f)
            )

            // Crosshair lines
            drawLine(
                color = LavenderAccent.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 2f
            )
            drawLine(
                color = LavenderAccent.copy(alpha = 0.2f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 2f
            )

            // Center User Dot
            drawCircle(color = VioletPrimary, radius = 16f, center = center)
            drawCircle(color = Color.White, radius = 8f, center = center)
        }

        // Rotating Compass Arrow pointing toward pickup person
        Box(
            modifier = Modifier
                .size(160.dp)
                .rotate(radarState.bearingDegrees),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = CircleShape,
                color = MintGreenSafety,
                shadowElevation = 8.dp,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Direction Arrow",
                        tint = DarkBackground,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Radar Distance & Status Card
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard.copy(alpha = 0.95f)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(MintGreenSafety, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = "Walking",
                            tint = DarkBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = personName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = radarState.statusText,
                            fontSize = 12.sp,
                            color = LavenderAccent
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${Math.round(radarState.distanceMeters)}m",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MintGreenSafety
                    )
                    Text(
                        text = radarState.estimatedWalkTime,
                        fontSize = 11.sp,
                        color = LavenderAccent
                    )
                }
            }
        }
    }
}
