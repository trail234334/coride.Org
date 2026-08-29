package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

import com.example.data.OverlayEntity

@Composable
fun InteractiveGoogleMapView(
    modifier: Modifier = Modifier,
    originName: String = "CET Main Gate, Sreekaryam",
    destName: String = "Thampanoor Bus Terminal, TVM",
    driverName: String = "Alex Morgan",
    showDriverMarker: Boolean = true,
    showRadarZone: Boolean = false,
    customOverlays: List<OverlayEntity> = emptyList(),
    onMapClick: () -> Unit = {}
) {
    var isTrafficOn by remember { mutableStateOf(true) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE8ECEF))
            .border(1.dp, LavenderAccent, RoundedCornerShape(20.dp))
            .clickable { onMapClick() }
            .testTag("google_map_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Background map surface grid
            drawRect(color = Color(0xFFEDF2F4))

            // Draw Custom Polygon & Tile Overlays
            customOverlays.filter { it.isVisible }.forEach { overlay ->
                when (overlay.type) {
                    "POLYGON" -> {
                        // Polygon Safe Zone
                        val polyPath = Path().apply {
                            moveTo(width * 0.3f, height * 0.3f)
                            lineTo(width * 0.7f, height * 0.28f)
                            lineTo(width * 0.65f, height * 0.65f)
                            lineTo(width * 0.25f, height * 0.6f)
                            close()
                        }
                        drawPath(
                            path = polyPath,
                            color = MintGreenSafety.copy(alpha = 0.25f)
                        )
                        drawPath(
                            path = polyPath,
                            color = MintGreenSafety,
                            style = Stroke(width = 4f * zoomLevel)
                        )
                    }
                    "TILE_LAYER" -> {
                        // Custom Shuttle Route Tile Polyline
                        val tilePath = Path().apply {
                            moveTo(width * 0.1f, height * 0.5f)
                            cubicTo(width * 0.3f, height * 0.2f, width * 0.7f, height * 0.8f, width * 0.9f, height * 0.4f)
                        }
                        drawPath(
                            path = tilePath,
                            color = Color(0xFFE11D48),
                            style = Stroke(width = 8f * zoomLevel, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f))
                        )
                    }
                    "MARKER" -> {
                        // Custom Pin Marker
                        val pinPos = Offset(width * 0.55f, height * 0.35f)
                        drawCircle(color = VioletPrimary, radius = 18f * zoomLevel, center = pinPos)
                        drawCircle(color = Color.White, radius = 8f * zoomLevel, center = pinPos)
                    }
                }
            }

            // Draw Parks / Campus areas
            drawCircle(
                color = Color(0xFFD8F3DC),
                radius = width * 0.25f,
                center = Offset(width * 0.2f, height * 0.3f)
            )

            // Draw roads
            val mainRoadPath = Path().apply {
                moveTo(width * 0.1f, height * 0.85f)
                cubicTo(
                    width * 0.35f, height * 0.8f,
                    width * 0.45f, height * 0.2f,
                    width * 0.88f, height * 0.15f
                )
            }

            // Outer road border
            drawPath(
                path = mainRoadPath,
                color = Color.White,
                style = Stroke(width = 24f * zoomLevel)
            )

            // Inner road lane
            drawPath(
                path = mainRoadPath,
                color = if (isTrafficOn) Color(0xFF93C5FD) else Color(0xFFCBD5E1),
                style = Stroke(width = 16f * zoomLevel)
            )

            // Traffic congestion segments
            val trafficSegment = Path().apply {
                moveTo(width * 0.4f, height * 0.55f)
                cubicTo(
                    width * 0.45f, height * 0.35f,
                    width * 0.55f, height * 0.25f,
                    width * 0.65f, height * 0.22f
                )
            }
            if (isTrafficOn) {
                drawPath(
                    path = trafficSegment,
                    color = Color(0xFFF87171), // Red traffic
                    style = Stroke(width = 16f * zoomLevel)
                )
            }

            // CoRide Active Route Polyline (Deep Violet gradient style)
            val routePath = Path().apply {
                moveTo(width * 0.15f, height * 0.82f)
                cubicTo(
                    width * 0.38f, height * 0.78f,
                    width * 0.48f, height * 0.25f,
                    width * 0.85f, height * 0.18f
                )
            }

            drawPath(
                path = routePath,
                color = VioletPrimary,
                style = Stroke(width = 10f * zoomLevel)
            )

            // Dashed route extension
            drawPath(
                path = routePath,
                color = VioletLight,
                style = Stroke(
                    width = 6f * zoomLevel,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )
            )

            // Origin Marker Pin (Stanford)
            val originPos = Offset(width * 0.15f, height * 0.82f)
            drawCircle(color = VioletPrimary, radius = 14f * zoomLevel, center = originPos)
            drawCircle(color = Color.White, radius = 6f * zoomLevel, center = originPos)

            // Destination Marker Pin (SFO)
            val destPos = Offset(width * 0.85f, height * 0.18f)
            drawCircle(color = MintGreenSafety, radius = 16f * zoomLevel, center = destPos)
            drawCircle(color = Color.White, radius = 7f * zoomLevel, center = destPos)

            // Moving Driver Vehicle Marker
            if (showDriverMarker) {
                val driverPos = Offset(width * 0.46f, height * 0.45f)

                // Pulsing GPS ripple
                drawCircle(
                    color = VioletLight.copy(alpha = 0.3f),
                    radius = 32f * zoomLevel,
                    center = driverPos
                )
                drawCircle(
                    color = VioletPrimary,
                    radius = 18f * zoomLevel,
                    center = driverPos
                )
                drawCircle(
                    color = Color.White,
                    radius = 8f * zoomLevel,
                    center = driverPos
                )
            }

            // Optional Radar Zone Circle
            if (showRadarZone) {
                val radarCenter = Offset(width * 0.5f, height * 0.5f)
                drawCircle(
                    color = MintGreenSafety.copy(alpha = 0.15f),
                    radius = 70f * zoomLevel,
                    center = radarCenter
                )
                drawCircle(
                    color = MintGreenSafety,
                    radius = 70f * zoomLevel,
                    center = radarCenter,
                    style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )
            }
        }

        // Map Top Watermark & Live GPS Pill
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Map Location",
                tint = VioletPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Google Maps • Live Traffic",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CharcoalText
            )
        }

        // Map Controls (Traffic toggle, Zoom)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                onClick = { isTrafficOn = !isTrafficOn },
                shape = CircleShape,
                color = if (isTrafficOn) VioletPrimary else Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Traffic,
                        contentDescription = "Toggle Traffic",
                        tint = if (isTrafficOn) Color.White else CharcoalText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Surface(
                onClick = { zoomLevel = if (zoomLevel >= 1.4f) 1.0f else zoomLevel + 0.2f },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom In",
                        tint = CharcoalText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Bottom Route Overlay Card
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(VioletPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = originName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MintGreenSafety, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = destName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MutedGrayText,
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = LavenderSurface
                ) {
                    Text(
                        text = "18.4 km",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
