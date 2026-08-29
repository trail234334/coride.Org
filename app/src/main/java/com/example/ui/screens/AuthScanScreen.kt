package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.CoRideUiState
import com.example.ui.components.CollegeIdScanOverlay
import com.example.ui.theme.*

@Composable
fun AuthScanScreen(
    uiState: CoRideUiState,
    onScanClick: (Int) -> Unit,
    onScanStaffClick: (Int) -> Unit = {},
    onContinueToHome: () -> Unit,
    onLoginWithStudentId: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onLoginWithStaffId: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var sampleCounter by remember { mutableIntStateOf(0) }
    var loginType by remember { mutableStateOf("student") } // "student" or "staff"

    var studentIdInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var collegeInput by remember { mutableStateOf("") }
    var deptInput by remember { mutableStateOf("") }

    var staffIdInput by remember { mutableStateOf("") }
    var staffNameInput by remember { mutableStateOf("") }
    var staffCollegeInput by remember { mutableStateOf("") }
    var staffDeptInput by remember { mutableStateOf("") }

    var showCameraScan by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBase)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("auth_scan_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LavenderSurface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (loginType == "student") Icons.Default.Badge else Icons.Default.SupervisorAccount,
                    contentDescription = "Login ID",
                    tint = VioletPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (loginType == "student") "Student ID Login" else "Staff ID Login",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CharcoalText
                )
                Text(
                    text = if (loginType == "student") "Enter your Official College Student ID to access CoRide" else "Enter your Official College Staff ID & Name to access CoRide",
                    fontSize = 12.sp,
                    color = MutedGrayText
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Type Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LavenderSurface, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TabButton(
                title = "Student Login",
                icon = Icons.Default.School,
                selected = loginType == "student",
                onClick = {
                    loginType = "student"
                    errorMessage = null
                },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                title = "Staff Login",
                icon = Icons.Default.Work,
                selected = loginType == "staff",
                onClick = {
                    loginType = "staff"
                    errorMessage = null
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, LavenderAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (loginType == "student") Icons.Default.PermIdentity else Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = VioletPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (loginType == "student") "College Student Credentials" else "College Staff Credentials",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalText
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (loginType == "student") {
                    OutlinedTextField(
                        value = studentIdInput,
                        onValueChange = { studentIdInput = it },
                        label = { Text("College Student ID / Register No.*") },
                        placeholder = { Text("e.g. TLY25CS007") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_id_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = LavenderSurface)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Student Full Name*") },
                        placeholder = { Text("e.g. Rahul Sharma") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = collegeInput,
                        onValueChange = { collegeInput = it },
                        label = { Text("College / Campus Name*") },
                        placeholder = { Text("e.g. College of Engineering Trivandrum") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_college_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = deptInput,
                        onValueChange = { deptInput = it.uppercase() },
                        label = { Text("Department (CSE, ME, CE, ECE, EE)*") },
                        placeholder = { Text("e.g. CSE") },
                        leadingIcon = { Icon(Icons.Default.Class, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_dept_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val depts = listOf("CSE", "ME", "CE", "ECE", "EE")
                        depts.forEach { dept ->
                            FilterChip(
                                selected = deptInput == dept,
                                onClick = { deptInput = dept },
                                label = { Text(dept, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VioletPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = LavenderSurface,
                                    labelColor = VioletPrimary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = deptInput == dept,
                                    borderColor = LavenderAccent,
                                    selectedBorderColor = VioletPrimary
                                )
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = staffIdInput,
                        onValueChange = { staffIdInput = it },
                        label = { Text("Staff ID / Employee ID*") },
                        placeholder = { Text("e.g. STAFF-CET-8821") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("staff_id_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = LavenderSurface)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = staffNameInput,
                        onValueChange = { staffNameInput = it },
                        label = { Text("Staff Full Name*") },
                        placeholder = { Text("e.g. Dr. Anitha Nair") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("staff_name_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = staffCollegeInput,
                        onValueChange = { staffCollegeInput = it },
                        label = { Text("College / Campus Name*") },
                        placeholder = { Text("e.g. College of Engineering Trivandrum") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("staff_college_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = staffDeptInput,
                        onValueChange = { staffDeptInput = it },
                        label = { Text("Department / Designation*") },
                        placeholder = { Text("e.g. Computer Science Dept") },
                        leadingIcon = { Icon(Icons.Default.WorkOutline, contentDescription = null, tint = VioletPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("staff_dept_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (loginType == "student") {
                            val validDepts = listOf("CSE", "ME", "CE", "ECE", "EE")
                            val cleanDept = deptInput.uppercase().trim()
                            val studentIdRegex = Regex("^[A-Za-z]{3}[0-9]{2}[A-Za-z]{2}[0-9]{3}$")
                            if (studentIdInput.isBlank()) {
                                errorMessage = "Error: College Student ID / Register Number is required!"
                            } else if (!studentIdRegex.matches(studentIdInput.trim())) {
                                errorMessage = "Error: Student ID format must be like TLY25CS007 (3 letters, 2 digits, 2 letters, 3 digits)"
                            } else if (nameInput.isBlank()) {
                                errorMessage = "Error: Student Full Name is required!"
                            } else if (collegeInput.isBlank()) {
                                errorMessage = "Error: College / Campus Name is required!"
                            } else if (cleanDept.isBlank() || !validDepts.contains(cleanDept)) {
                                errorMessage = "Error: Department must be one of CSE, ME, CE, ECE, EE in capital letters!"
                            } else {
                                errorMessage = null
                                onLoginWithStudentId(studentIdInput, nameInput, collegeInput, cleanDept)
                                onContinueToHome()
                            }
                        } else {
                            if (staffIdInput.isBlank()) {
                                errorMessage = "Error: Staff ID / Employee ID is required!"
                            } else if (staffNameInput.isBlank()) {
                                errorMessage = "Error: Staff Full Name is required!"
                            } else if (staffCollegeInput.isBlank()) {
                                errorMessage = "Error: College / Campus Name is required!"
                            } else if (staffDeptInput.isBlank()) {
                                errorMessage = "Error: Department / Designation is required!"
                            } else {
                                errorMessage = null
                                onLoginWithStaffId(staffIdInput, staffNameInput, staffCollegeInput, staffDeptInput)
                                onContinueToHome()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("auth_continue_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                ) {
                    Text(
                        text = if (loginType == "student") "Verify & Login as Student" else "Verify & Login as Staff",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message banner if validation fails
        errorMessage?.let { err ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = err, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Scan ID Button that opens camera dialog
        Button(
            onClick = { showCameraScan = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("scan_id_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LavenderSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VioletPrimary)
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = VioletPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (loginType == "student") "Scan Student ID Card" else "Scan Staff ID Card",
                color = VioletPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        if (showCameraScan) {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { showCameraScan = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CollegeIdScanOverlay(
                            isScanning = uiState.isScanningId,
                            extractedName = if (loginType == "student") uiState.currentUser.name else staffNameInput,
                            extractedCollege = if (loginType == "student") uiState.currentUser.collegeName else staffCollegeInput,
                            scanTitle = if (loginType == "student") "Align Student ID Card in Frame" else "Align Staff ID Card in Frame",
                            buttonText = if (loginType == "student") "Scan Student ID" else "Scan Staff ID",
                            onScanClick = {
                                sampleCounter++
                                if (loginType == "student") {
                                    onScanClick(sampleCounter)
                                    studentIdInput = "TLY25CS007"
                                    nameInput = "Alex Morgan"
                                    collegeInput = "College of Engineering Trivandrum"
                                    deptInput = "CSE"
                                } else {
                                    onScanStaffClick(sampleCounter)
                                    staffIdInput = "EMP-9021"
                                    staffNameInput = "Dr. Robert Matthews"
                                    staffCollegeInput = "College of Engineering Trivandrum"
                                    staffDeptInput = "Computer Science"
                                }
                                showCameraScan = false
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Close button at top right
                        IconButton(
                            onClick = { showCameraScan = false },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(24.dp)
                                .background(Color.White.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close Camera", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) VioletPrimary else Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (selected) Color.White else CharcoalText
            )
        }
    }
}
