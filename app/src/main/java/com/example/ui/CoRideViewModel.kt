package com.example.ui

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.CarDatabase
import com.example.data.AppDatabase
import com.example.data.OverlayEntity
import com.example.data.RideEntity
import com.example.data.RideRepository
import com.example.model.*
import com.example.service.OcrScannerService
import com.example.service.RadarTrackingService
import com.example.service.RadarTrackingService.RadarPositionState
import com.example.service.RouteMatchingEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CoRideUiState(
    val currentTabScreen: String = "auth", // splash, auth, home, offer, request, bidding, radar, chat, fuel, history, safety
    val currentUser: User = User(),
    val isScanningId: Boolean = false,
    val ocrScanResult: OcrScannerService.OcrScanResult? = null,
    val rideOffers: List<RideOffer> = emptyList(),
    val selectedRide: RideOffer? = null,
    val bookedRideIds: Set<String> = emptySet(),
    val activeBids: List<FareBid> = emptyList(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val selectedCarModel: CarModelSpec = CarDatabase.getDefaultCar(),
    val availableCarsList: List<CarModelSpec> = CarDatabase.getAllCars(),
    val carSearchQuery: String = "",
    val customCarKmPerLiter: Double = 22.5,
    val fuelPricePerLiter: Double = 105.50,
    val tripDistanceKm: Double = 18.4,
    val passengersCount: Int = 3,
    val fuelCalculation: FuelCalculation = FuelCalculation(18.4, 22.5, 105.50, 3),
    val radarState: RadarPositionState = RadarPositionState(120.0, 45f, "Walking towards CET pickup zone", false, "2 min walk"),
    val activeDriverLat: Double = 8.5475,
    val activeDriverLng: Double = 76.9063,
    val userLat: Double = 8.5490,
    val userLng: Double = 76.9080,
    val emergencySosTriggered: Boolean = false,
    val isDarkMode: Boolean = false,
    val activeBidTimerSeconds: Int = 180,
    val customOverlays: List<OverlayEntity> = emptyList(),
    val streetViewLat: Double = 8.5475,
    val streetViewLng: Double = 76.9063,
    val streetViewHeading: Float = 45f,
    val streetViewPitch: Float = 5f,
    val streetViewZoom: Float = 1.0f,
    val streetViewAddress: String = "CET Main Gate, Sreekaryam, TVM",
    val serverStatus: com.example.data.api.ServerStatusResponse? = null,
    val isCheckingServer: Boolean = false,
    val isSyncingBackend: Boolean = false,
    val backendSyncMessage: String? = null
)

private val Context.dataStore by preferencesDataStore(name = "coride_user_datastore")

private object DataStoreKeys {
    val IS_VERIFIED = booleanPreferencesKey("is_verified")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_COLLEGE = stringPreferencesKey("user_college")
    val USER_STUDENT_ID = stringPreferencesKey("user_student_id")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_DEPT = stringPreferencesKey("user_dept")
    val USER_RATING = doublePreferencesKey("user_rating")
    val USER_RIDES = intPreferencesKey("user_rides")
}

class CoRideViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CoRideUiState())
    val uiState: StateFlow<CoRideUiState> = _uiState.asStateFlow()

    private val db = AppDatabase.getDatabase(application)
    private val rideRepository = RideRepository(db.rideDao())
    private val serverRepository = com.example.data.api.CoRideServerRepository(rideRepository)
    private val realtimeClient = com.example.data.api.CoRideRealtimeClient(rideRepository)
    private val dataStore = application.applicationContext.dataStore

    init {
        loadPersistedUser()
        initRoomDatabase()
        loadDefaultOverlays()
        startRadarTickLoop()
        checkServerHealth()
        syncBackendData()
        realtimeClient.start()
    }

    private fun loadPersistedUser() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            val isVerified = prefs[DataStoreKeys.IS_VERIFIED] ?: false
            if (isVerified) {
                val id = prefs[DataStoreKeys.USER_ID] ?: ""
                val name = prefs[DataStoreKeys.USER_NAME] ?: ""
                val college = prefs[DataStoreKeys.USER_COLLEGE] ?: ""
                val studentId = prefs[DataStoreKeys.USER_STUDENT_ID] ?: ""
                val email = prefs[DataStoreKeys.USER_EMAIL] ?: ""
                val dept = prefs[DataStoreKeys.USER_DEPT] ?: ""
                val rating = prefs[DataStoreKeys.USER_RATING] ?: 4.95
                val rides = prefs[DataStoreKeys.USER_RIDES] ?: 12

                val savedUser = User(id, name, college, studentId, email, dept, rating, rides, true)
                _uiState.update {
                    it.copy(
                        currentUser = savedUser,
                        currentTabScreen = "home"
                    )
                }
            }
        }
    }

    private fun persistUser(user: User) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[DataStoreKeys.IS_VERIFIED] = user.isVerified()
                prefs[DataStoreKeys.USER_ID] = user.getId()
                prefs[DataStoreKeys.USER_NAME] = user.getName()
                prefs[DataStoreKeys.USER_COLLEGE] = user.getCollegeName()
                prefs[DataStoreKeys.USER_STUDENT_ID] = user.getStudentIdNumber()
                prefs[DataStoreKeys.USER_EMAIL] = user.getEmail()
                prefs[DataStoreKeys.USER_DEPT] = user.getDepartment()
                prefs[DataStoreKeys.USER_RATING] = user.getRating()
                prefs[DataStoreKeys.USER_RIDES] = user.getRidesCompleted()
            }
        }
    }

    fun checkServerHealth() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingServer = true) }
            val status = serverRepository.checkServerHealth()
            _uiState.update { it.copy(serverStatus = status, isCheckingServer = false) }
        }
    }

    fun syncBackendData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncingBackend = true, backendSyncMessage = "Syncing with CoRide Cloud Backend...") }
            val synced = serverRepository.syncRidesFromServer()
            val msg = if (synced.isNotEmpty()) "Successfully synced ${synced.size} rides from Cloud Backend!" else "Backend connected (Offline-First sync active)."
            _uiState.update { it.copy(isSyncingBackend = false, backendSyncMessage = msg) }
        }
    }


    private fun initRoomDatabase() {
        viewModelScope.launch {
            // Observe real Room Database changes
            rideRepository.allRides.collect { entities ->
                val offers = entities.map { it.toRideOffer() }
                _uiState.update { current ->
                    current.copy(
                        rideOffers = offers,
                        selectedRide = current.selectedRide ?: offers.firstOrNull()
                    )
                }
            }
        }
    }

    private fun loadDefaultOverlays() {
        val defaults = listOf(
            OverlayEntity(
                id = "ov_01",
                type = "MARKER",
                title = "CET Student Safe Pickup Point",
                description = "Well-lit waiting bay near CET Main Gate Sreekaryam",
                category = "Pickup Point",
                colorHex = "#005AC1",
                pointsJson = "8.5475,76.9063",
                iconType = "PIN",
                isVisible = true
            ),
            OverlayEntity(
                id = "ov_02",
                type = "POLYGON",
                title = "CET Campus Night Safety Corridor",
                description = "Verified campus patrol zone with 24/7 security guard presence",
                category = "Campus Safe Zone",
                colorHex = "#00875A",
                pointsJson = "8.5460,76.9040;8.5500,76.9050;8.5490,76.9090;8.5450,76.9080",
                iconType = "SHIELD",
                isVisible = true
            ),
            OverlayEntity(
                id = "ov_03",
                type = "TILE_LAYER",
                title = "Technopark Shuttle Line Tile Layer",
                description = "High-frequency student shuttle overlay connecting CET & Technopark",
                category = "Campus Shuttle Layer",
                colorHex = "#BA1A1A",
                pointsJson = "8.5475,76.9063;8.5520,76.8950;8.5566,76.8820",
                iconType = "BUS",
                isVisible = true
            )
        )
        _uiState.update { it.copy(customOverlays = defaults) }
    }

    fun addCustomOverlay(overlay: OverlayEntity) {
        _uiState.update { it.copy(customOverlays = listOf(overlay) + it.customOverlays) }
    }

    fun deleteCustomOverlay(id: String) {
        _uiState.update { currentState ->
            currentState.copy(customOverlays = currentState.customOverlays.filterNot { it.id == id })
        }
    }

    fun toggleOverlayVisibility(id: String) {
        _uiState.update { currentState ->
            val updated = currentState.customOverlays.map { ov ->
                if (ov.id == id) ov.copy(isVisible = !ov.isVisible) else ov
            }
            currentState.copy(customOverlays = updated)
        }
    }

    fun updateStreetViewLocation(lat: Double, lng: Double, address: String) {
        _uiState.update { 
            it.copy(
                streetViewLat = lat,
                streetViewLng = lng,
                streetViewAddress = address
            )
        }
    }

    fun navigateStreetView(headingDelta: Float = 0f, pitchDelta: Float = 0f, stepDistance: Double = 0.0) {
        _uiState.update { current ->
            val newHeading = (current.streetViewHeading + headingDelta + 360f) % 360f
            val newPitch = (current.streetViewPitch + pitchDelta).coerceIn(-45f, 45f)
            
            val rad = Math.toRadians(newHeading.toDouble())
            val deltaLat = stepDistance * Math.cos(rad) * 0.0001
            val deltaLng = stepDistance * Math.sin(rad) * 0.0001
            
            current.copy(
                streetViewHeading = newHeading,
                streetViewPitch = newPitch,
                streetViewLat = current.streetViewLat + deltaLat,
                streetViewLng = current.streetViewLng + deltaLng
            )
        }
    }

    fun setScreen(screenName: String) {
        _uiState.update { it.copy(currentTabScreen = screenName) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun scanStudentIdCard(sampleIndex: Int = 0) {
        _uiState.update { it.copy(isScanningId = true) }
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isScanningId = false) }
        }
    }

    fun scanStaffIdCard(sampleIndex: Int = 0) {
        _uiState.update { it.copy(isScanningId = true) }
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isScanningId = false) }
        }
    }

    fun loginWithStudentId(
        studentIdNumber: String,
        name: String,
        collegeName: String,
        department: String
    ) {
        val cleanId = studentIdNumber.trim()
        val cleanName = name.trim()
        val cleanCollege = collegeName.trim()
        val cleanDept = department.trim()
        val generatedEmail = cleanId.lowercase().replace(Regex("[^a-z0-9]"), "") + "@cet.ac.in"

        val updatedUser = User(
            "usr_" + (System.currentTimeMillis() % 1000),
            cleanName,
            cleanCollege,
            cleanId,
            generatedEmail,
            cleanDept,
            4.95,
            12,
            true
        )

        val scanResult = OcrScannerService.OcrScanResult(
            true,
            cleanName,
            cleanCollege,
            cleanId,
            generatedEmail,
            cleanDept,
            "VERIFIED_STUDENT_ID: $cleanId"
        )

        persistUser(updatedUser)
        _uiState.update {
            it.copy(
                currentUser = updatedUser,
                ocrScanResult = scanResult,
                currentTabScreen = "home"
            )
        }
    }

    fun loginWithStaffId(
        staffIdNumber: String,
        name: String,
        collegeName: String,
        department: String
    ) {
        val cleanId = staffIdNumber.trim()
        val cleanName = name.trim()
        val cleanCollege = collegeName.trim()
        val cleanDept = department.trim()
        val generatedEmail = cleanId.lowercase().replace(Regex("[^a-z0-9]"), "") + "@cet.ac.in"

        val updatedUser = User(
            "staff_" + (System.currentTimeMillis() % 1000),
            cleanName,
            cleanCollege,
            cleanId,
            generatedEmail,
            "Staff - $cleanDept",
            5.0,
            45,
            true
        )

        val scanResult = OcrScannerService.OcrScanResult(
            true,
            cleanName,
            cleanCollege,
            cleanId,
            generatedEmail,
            "Staff - $cleanDept",
            "VERIFIED_STAFF_ID: $cleanId"
        )

        persistUser(updatedUser)
        _uiState.update {
            it.copy(
                currentUser = updatedUser,
                ocrScanResult = scanResult,
                currentTabScreen = "home"
            )
        }
    }

    fun createRideOffer(
        origin: String,
        destination: String,
        availableSeats: Int,
        isBiddingAllowed: Boolean,
        pricePerSeat: Double
    ) {
        val userName = _uiState.value.currentUser.name.ifBlank { "Alex Morgan" }
        val userDept = if (_uiState.value.currentUser.department.isNullOrBlank()) "CET College" else _uiState.value.currentUser.department
        val newOffer = RideOffer(
            "ride_" + (System.currentTimeMillis() % 10000),
            userName,
            userDept,
            4.98,
            true,
            _uiState.value.selectedCarModel.getFullName(),
            "KL-01-CB-4091",
            origin.ifBlank { "CET Main Gate, Sreekaryam" },
            destination.ifBlank { "Thampanoor Bus Terminal, TVM" },
            8.5475, 76.9063,
            8.4870, 76.9528,
            18.4,
            availableSeats,
            availableSeats,
            pricePerSeat,
            "05:00 PM Today",
            isBiddingAllowed,
            "UPCOMING"
        )
        viewModelScope.launch {
            rideRepository.insert(RideEntity.fromRideOffer(newOffer))
        }
        _uiState.update { current ->
            current.copy(selectedRide = newOffer)
        }
    }

    fun deleteRideOffer(rideId: String) {
        viewModelScope.launch {
            rideRepository.deleteById(rideId)
        }
    }

    fun clearAllRidesFromDb() {
        viewModelScope.launch {
            rideRepository.deleteAll()
        }
    }


    fun updateCarSearch(query: String) {
        val searchResults = CarDatabase.searchCars(query)
        _uiState.update { it.copy(carSearchQuery = query, availableCarsList = searchResults) }
    }

    fun selectCarModel(car: CarModelSpec) {
        _uiState.update {
            val calc = FuelCalculation(it.tripDistanceKm, car.avgKmPerLiter, it.fuelPricePerLiter, it.passengersCount)
            it.copy(
                selectedCarModel = car,
                customCarKmPerLiter = car.avgKmPerLiter,
                fuelCalculation = calc
            )
        }
    }

    fun updateFuelInputs(distanceKm: Double, kmPerL: Double, pricePerL: Double, passengers: Int) {
        val calc = FuelCalculation(distanceKm, kmPerL, pricePerL, passengers)
        _uiState.update {
            it.copy(
                tripDistanceKm = distanceKm,
                customCarKmPerLiter = kmPerL,
                fuelPricePerLiter = pricePerL,
                passengersCount = passengers,
                fuelCalculation = calc
            )
        }
    }

    fun placeFareBid(rideId: String, bidAmount: Double) {
        val newBid = FareBid(
            "bid_" + System.currentTimeMillis() % 1000,
            rideId,
            _uiState.value.currentUser.name,
            _uiState.value.currentUser.department,
            _uiState.value.currentUser.rating,
            bidAmount,
            _uiState.value.selectedRide?.basePricePerSeat ?: 8.50,
            "PENDING",
            "Just now",
            180
        )
        _uiState.update { it.copy(activeBids = listOf(newBid) + it.activeBids) }
    }

    fun updateBidStatus(bidId: String, newStatus: String) {
        _uiState.update { currentState ->
            val updatedBids = currentState.activeBids.map { bid ->
                if (bid.id == bidId) {
                    FareBid(bid.id, bid.rideId, bid.passengerName, bid.passengerCollege, 
                            bid.passengerRating, bid.bidAmount, bid.askingPrice, 
                            newStatus, bid.timestamp, bid.secondsRemaining)
                } else bid
            }
            currentState.copy(activeBids = updatedBids)
        }
    }

    fun selectRideForChat(ride: RideOffer) {
        _uiState.update { it.copy(selectedRide = ride) }
    }

    fun bookRide(rideId: String) {
        _uiState.update { current ->
            val newBooked = current.bookedRideIds + rideId
            val targetRide = current.rideOffers.find { it.id == rideId } ?: current.selectedRide
            current.copy(
                bookedRideIds = newBooked,
                selectedRide = targetRide
            )
        }
    }

    fun sendChatMessage(text: String, isLocationPin: Boolean = false) {
        val currentRide = _uiState.value.selectedRide
        val driverName = currentRide?.driverName ?: "Alex Morgan"
        val rideId = currentRide?.id ?: "ride_101"
        val userName = if (_uiState.value.currentUser.name.isNotBlank()) _uiState.value.currentUser.name else "You"

        val newMsg = if (isLocationPin) {
            ChatMessage(
                "msg_" + System.currentTimeMillis() % 1000,
                rideId,
                userName,
                false,
                true,
                "Shared Live Pickup Location Pin",
                "Just now",
                _uiState.value.userLat,
                _uiState.value.userLng
            )
        } else {
            ChatMessage(
                "msg_" + System.currentTimeMillis() % 1000,
                rideId,
                userName,
                false,
                true,
                text,
                "Just now"
            )
        }

        _uiState.update { it.copy(chatMessages = it.chatMessages + newMsg) }

        // Trigger realistic automated reply from rider/driver
        viewModelScope.launch {
            delay(1200)
            val replyText = when {
                isLocationPin -> "Got your live pickup pin! Navigating towards it now 📍"
                text.contains("gate", ignoreCase = true) || text.contains("standing", ignoreCase = true) -> "Great! I'm near CET main gate. See you in 1 min! 🚘"
                text.contains("time", ignoreCase = true) || text.contains("where", ignoreCase = true) || text.contains("late", ignoreCase = true) -> "Just crossed Sreekaryam signal, almost there! ⏳"
                text.contains("price", ignoreCase = true) || text.contains("fare", ignoreCase = true) || text.contains("bid", ignoreCase = true) -> "Fare split is set! Ready when you are."
                text.contains("hi", ignoreCase = true) || text.contains("hello", ignoreCase = true) || text.contains("hey", ignoreCase = true) -> "Hey! Ready for the ride from campus?"
                else -> "Awesome! On my way to pick you up."
            }

            val driverReply = ChatMessage(
                "msg_reply_" + System.currentTimeMillis() % 1000,
                rideId,
                "$driverName (Driver)",
                true,
                true,
                replyText,
                "Just now"
            )
            _uiState.update { it.copy(chatMessages = it.chatMessages + driverReply) }
        }
    }

    fun triggerEmergencySos() {
        _uiState.update { it.copy(emergencySosTriggered = true) }
    }

    fun dismissEmergencySos() {
        _uiState.update { it.copy(emergencySosTriggered = false) }
    }

    private fun startRadarTickLoop() {
        viewModelScope.launch {
            var step = 0
            while (true) {
                delay(1000)
                step++
                val driverLat = 8.5475 + (step % 20) * 0.0001
                val driverLng = 76.9063 + (step % 20) * 0.0001
                val userLat = 8.5490
                val userLng = 76.9080

                val radar = RadarTrackingService.calculateRadarState(
                    userLat, userLng, driverLat, driverLng, 1f
                )

                _uiState.update {
                    it.copy(
                        radarState = radar,
                        activeDriverLat = driverLat,
                        activeDriverLng = driverLng
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeClient.stop()
    }
}
