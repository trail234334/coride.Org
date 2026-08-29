package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun SplashScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var onboardingStep by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "LogoPulse")
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LogoScale"
    )

    val onboardingItems = listOf(
        Triple("Exclusive & Verified", "Ride sharing exclusively for verified college students & staff using AI ID Card scanning.", Icons.Default.Shield),
        Triple("Smart Fare Bidding", "Propose your own fuel split fare or accept fixed ride prices transparently.", Icons.Default.DirectionsCar),
        Triple("Find My Pickup Radar", "AirTag-style live radar view ensures you meet your pickup person seamlessly.", Icons.Default.LocationOn)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, VioletPrimary, DarkSurface)
                )
            )
            .testTag("splash_screen_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo & Brand Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(logoScale)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(VioletLight, LavenderAccent)
                            )
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon_1785768639257),
                        contentDescription = "CoRide Logo Mark",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CoRide",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Smart College Ride Sharing Platform",
                    fontSize = 14.sp,
                    color = LavenderAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Onboarding Card Carousel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .testTag("onboarding_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(LavenderSurface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = onboardingItems[onboardingStep].third,
                            contentDescription = "Feature Icon",
                            tint = VioletPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = onboardingItems[onboardingStep].first,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = onboardingItems[onboardingStep].second,
                        fontSize = 13.sp,
                        color = LavenderAccent,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        onboardingItems.indices.forEach { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == onboardingStep) 24.dp else 8.dp, 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == onboardingStep) MintGreenSafety else LavenderAccent.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }
                }
            }

            // Bottom CTA Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (onboardingStep < onboardingItems.size - 1) {
                            onboardingStep++
                        } else {
                            onGetStartedClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("splash_continue_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletLight)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (onboardingStep < onboardingItems.size - 1) "Next Feature" else "Scan College ID to Begin",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }

                if (onboardingStep < onboardingItems.size - 1) {
                    TextButton(
                        onClick = { onGetStartedClick() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Skip Intro",
                            color = LavenderAccent,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
