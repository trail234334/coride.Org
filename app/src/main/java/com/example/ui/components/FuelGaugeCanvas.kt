package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FuelCalculation
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FuelGaugeCanvas(
    calculation: FuelCalculation,
    modifier: Modifier = Modifier
) {
    val animatedAngle by animateFloatAsState(
        targetValue = calculation.gaugeNeedleAngle,
        animationSpec = tween(durationMillis = 800),
        label = "GaugeNeedleAnimation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, LavenderAccent, RoundedCornerShape(24.dp))
            .testTag("fuel_gauge_card"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
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
                            .size(36.dp)
                            .background(LavenderSurface, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalGasStation,
                            contentDescription = "Fuel Gauge",
                            tint = VioletPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Mileage & Cost Gauge",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalText
                        )
                        Text(
                            text = "Live fuel efficiency calculator",
                            fontSize = 12.sp,
                            color = MutedGrayText
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LavenderSurface
                ) {
                    Text(
                        text = "${String.format("%.1f", calculation.kmPerLiter)} km/L",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gauge Meter Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val center = Offset(canvasWidth / 2f, canvasHeight * 0.85f)
                    val radius = canvasWidth * 0.38f

                    // Background Gauge Arc (180 degrees from 180° to 360°)
                    drawArc(
                        color = Color(0xFFE2E8F0),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )

                    // Efficiency Colored Arc Segments (Green = Eco high km/L, Yellow = Mid, Red = Low)
                    // High efficiency arc (Green)
                    drawArc(
                        color = MintGreenSafety,
                        startAngle = 180f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )

                    // Mid efficiency arc (Violet)
                    drawArc(
                        color = VioletLight,
                        startAngle = 240f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )

                    // Low efficiency arc (Amber)
                    drawArc(
                        color = Color(0xFFF59E0B),
                        startAngle = 300f,
                        sweepAngle = 60f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )

                    // Needle pointer calculation
                    val rad = Math.toRadians((180f + animatedAngle).toDouble())
                    val needleLen = radius * 0.85f
                    val needleEnd = Offset(
                        (center.x + needleLen * cos(rad)).toFloat(),
                        (center.y + needleLen * sin(rad)).toFloat()
                    )

                    // Draw Needle Line
                    drawLine(
                        color = VioletPrimary,
                        start = center,
                        end = needleEnd,
                        strokeWidth = 8f,
                        cap = StrokeCap.Round
                    )

                    // Needle Pivot Pin
                    drawCircle(color = VioletPrimary, radius = 12f, center = center)
                    drawCircle(color = Color.White, radius = 6f, center = center)
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "₹${String.format("%.2f", calculation.costPerPassenger)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VioletPrimary
                    )
                    Text(
                        text = "Cost per Person (${calculation.passengersCount + 1} split)",
                        fontSize = 11.sp,
                        color = MutedGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Breakdown Stats Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LavenderSurface, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${String.format("%.1f", calculation.fuelRequiredLiters)} L",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Text(
                        text = "Fuel Needed",
                        fontSize = 10.sp,
                        color = MutedGrayText
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp),
                    color = LavenderAccent
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "₹${String.format("%.2f", calculation.totalFuelCost)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                    Text(
                        text = "Total Trip Fuel",
                        fontSize = 10.sp,
                        color = MutedGrayText
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp),
                    color = LavenderAccent
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${String.format("%.1f", calculation.co2EmissionsSavedKg)} kg",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintGreenSafety
                    )
                    Text(
                        text = "CO2 Saved",
                        fontSize = 10.sp,
                        color = MutedGrayText
                    )
                }
            }
        }
    }
}
