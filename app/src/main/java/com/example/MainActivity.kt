package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CoRideViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    private val viewModel: CoRideViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            CoRideTheme(darkTheme = uiState.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (uiState.currentTabScreen != "splash" && uiState.currentTabScreen != "auth" && uiState.currentUser.isVerified) {
                            CoRideBottomNavigationBar(
                                currentScreen = uiState.currentTabScreen,
                                onTabSelect = { screen -> viewModel.setScreen(screen) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = if (uiState.currentTabScreen != "splash" && !uiState.currentUser.isVerified) "auth" else uiState.currentTabScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                "splash" -> SplashScreen(
                                    onGetStartedClick = { viewModel.setScreen("auth") }
                                )
                                "auth" -> AuthScanScreen(
                                    uiState = uiState,
                                    onScanClick = { sampleIdx -> viewModel.scanStudentIdCard(sampleIdx) },
                                    onScanStaffClick = { sampleIdx -> viewModel.scanStaffIdCard(sampleIdx) },
                                    onContinueToHome = { viewModel.setScreen("home") },
                                    onLoginWithStudentId = { id, name, college, dept ->
                                        viewModel.loginWithStudentId(id, name, college, dept)
                                        viewModel.setScreen("home")
                                    },
                                    onLoginWithStaffId = { id, name, college, dept ->
                                        viewModel.loginWithStaffId(id, name, college, dept)
                                        viewModel.setScreen("home")
                                    }
                                )
                                "home" -> HomeScreen(
                                    uiState = uiState,
                                    onNavigate = { target -> viewModel.setScreen(target) }
                                )
                                "offer" -> OfferRideScreen(
                                    uiState = uiState,
                                    onOfferCreated = { origin, dest, seats, bidding, price ->
                                        viewModel.createRideOffer(origin, dest, seats, bidding, price)
                                        viewModel.setScreen("home")
                                    }
                                )
                                "request" -> RequestRideScreen(
                                    uiState = uiState,
                                    onPlaceBid = { rideId, amount ->
                                        viewModel.placeFareBid(rideId, amount)
                                        viewModel.setScreen("bidding")
                                    },
                                    onBookRide = { rideId -> viewModel.bookRide(rideId) },
                                    onDeleteRide = { rideId -> viewModel.deleteRideOffer(rideId) }
                                )

                                "bidding" -> BiddingScreen(
                                    uiState = uiState,
                                    onBidAction = { id, status -> viewModel.updateBidStatus(id, status) }
                                )
                                "radar" -> PickupRadarScreen(
                                    uiState = uiState,
                                     onNavigateBack = { viewModel.setScreen("home") }
                                )
                                "chat" -> ChatScreen(
                                     uiState = uiState,
                                     onSendMessage = { text, isPin -> viewModel.sendChatMessage(text, isPin) },
                                     onSelectRide = { ride -> viewModel.selectRideForChat(ride) },
                                     onNavigateToRadar = { viewModel.setScreen("radar") }
                                 )
                                 "fuel" -> FuelCalculatorScreen(
                                    uiState = uiState,
                                    onCarSearch = { query -> viewModel.updateCarSearch(query) },
                                    onCarSelected = { car -> viewModel.selectCarModel(car) },
                                    onInputsChanged = { d, km, p, passengers ->
                                        viewModel.updateFuelInputs(d, km, p, passengers)
                                    }
                                )
                                "history" -> HistoryRatingsScreen(
                                    uiState = uiState
                                )
                                "safety" -> SafetyCenterScreen(
                                    uiState = uiState,
                                    onTriggerSos = { viewModel.triggerEmergencySos() }
                                )
                                else -> HomeScreen(
                                    uiState = uiState,
                                    onNavigate = { target -> viewModel.setScreen(target) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoRideBottomNavigationBar(
    currentScreen: String,
    onTabSelect: (String) -> Unit
) {
    val navigationItems = listOf(
        NavigationItem("home", "Home", Icons.Default.Home),
        NavigationItem("request", "Rides", Icons.Default.DirectionsCar),
        NavigationItem("bidding", "Bids", Icons.Default.Gavel),
        NavigationItem("chat", "Chat", Icons.Default.Chat)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = BorderOutline,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_navigation_bar")
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentScreen == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelect(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) VioletPrimary else MutedGrayText
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) VioletPrimary else MutedGrayText
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = LavenderSurface,
                    selectedIconColor = VioletPrimary,
                    unselectedIconColor = MutedGrayText,
                    selectedTextColor = VioletPrimary,
                    unselectedTextColor = MutedGrayText
                )
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
