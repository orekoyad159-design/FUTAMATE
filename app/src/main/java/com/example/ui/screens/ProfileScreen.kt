package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.Student
import com.example.ui.components.AccommodationStatusBadge
import com.example.ui.components.FutaVerifiedBadge
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    student: Student,
    currentRoommatesCount: Int,
    formerRoommatesCount: Int,
    onEditAccommodation: () -> Unit,
    onOpenRoommateHistory: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenUserSwitcher: () -> Unit,
    onTogglePhonePrivacy: () -> Unit,
    onToggleReligionPrivacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nairaFormatter = NumberFormat.getNumberInstance(Locale.US)
    var showPlayPrivacyPolicy by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var accountDeletedFeedback by remember { mutableStateOf(false) }

    if (showPlayPrivacyPolicy) {
        PlayStorePrivacyDialog(onDismiss = { showPlayPrivacyPolicy = false })
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(
                    text = "Request Account & Data Deletion",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "Per Google Play Store Policy and the Nigeria Data Protection Act (NDPA), you have the right to request deletion of your account, active roommate listings, chat histories, and matriculation data. Your request will be queued immediately.",
                    fontSize = 12.sp,
                    color = Neutral700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        accountDeletedFeedback = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Deletion Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (accountDeletedFeedback) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FutaEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Account deactivation request recorded. Data purge initiated.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }
        }

        // Profile Card Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Neutral200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(student.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.avatarInitials,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = student.fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        FutaVerifiedBadge(status = student.verificationStatus)
                    }

                    Text(
                        text = "${student.department} • ${student.level.label}",
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                    Text(
                        text = student.faculty,
                        fontSize = 11.sp,
                        color = Neutral500
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Matric Number Private Box (PRD Feature 1 & 7)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Neutral100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Private",
                                    tint = Neutral500,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Matric: ${student.matricNumber}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Neutral700
                                )
                            }
                            Text(
                                text = "Verified Student",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutaEmerald
                            )
                        }
                    }
                }
            }
        }

        // Feature 9: Roommate History Highlight Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FutaNavyDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("roommate_history_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = null,
                                tint = FutaGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Roommate History",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "$currentRoommatesCount Current • $formerRoommatesCount Former Roommates",
                            fontSize = 11.sp,
                            color = Neutral300
                        )
                        Text(
                            text = "Stay connected even after moving out.",
                            fontSize = 11.sp,
                            color = FutaGold
                        )
                    }

                    Button(
                        onClick = onOpenRoommateHistory,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FutaGoldDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Manage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Accommodation Status Section (Feature 3)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Neutral200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Accommodation Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        AccommodationStatusBadge(status = student.accommodationStatus)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = student.accommodationStatus.description,
                        fontSize = 12.sp,
                        color = Neutral700
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Area: ${student.preferredLocation}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FutaNavyDark
                        )
                        Text(
                            text = "Budget: ₦${nairaFormatter.format(student.budgetNaira)}/yr",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutaEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onEditAccommodation,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Update Accommodation Status & Rent")
                    }
                }
            }
        }

        // Privacy Controls Section (Feature 2 & 7)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Neutral200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Privacy & Student Disclosure",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Control what information is visible to other students",
                        fontSize = 11.sp,
                        color = Neutral500
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone privacy toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Public Phone Number",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (student.showPhonePublicly) "Visible to everyone (${student.phone})" else "Private (only shared upon mutual match)",
                                fontSize = 11.sp,
                                color = Neutral500
                            )
                        }
                        Switch(
                            checked = student.showPhonePublicly,
                            onCheckedChange = { onTogglePhonePrivacy() }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Neutral200)

                    // Religion privacy toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Disclose Religion on Profile",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (student.showReligionPublicly) "Shown as ${student.religion ?: "Disclosed"}" else "Hidden from roommate search cards",
                                fontSize = 11.sp,
                                color = Neutral500
                            )
                        }
                        Switch(
                            checked = student.showReligionPublicly,
                            onCheckedChange = { onToggleReligionPrivacy() }
                        )
                    }
                }
            }
        }

        // Google Play Compliance & Legal Terms
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Neutral200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Google Play Policy & Security",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Adheres to Google Play User Data Policy & Nigeria Data Protection Act (NDPA)",
                        fontSize = 11.sp,
                        color = Neutral500
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showPlayPrivacyPolicy = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Policy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Student Privacy Policy & NDPA Terms", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(
                        onClick = { showDeleteAccountDialog = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete Account & Personal Data", fontSize = 12.sp)
                    }
                }
            }
        }

        // Quick Actions & Admin Dashboard
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onOpenUserSwitcher,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Switch Active Demo Student Account")
                }

                Button(
                    onClick = onOpenAdminDashboard,
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyMedium),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Admin Moderation Dashboard")
                }
            }
        }
    }
}

@Composable
fun PlayStorePrivacyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Play Store Privacy & Terms",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = FutaNavyDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "1. Student Data Protection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FutaNavyDark
                    )
                    Text(
                        text = "FUTA Roommate Finder complies with Google Play's User Data Policy and the Nigeria Data Protection Act (NDPA). Matriculation numbers are gathered strictly for student identity validation and are never shared publicly or sold.",
                        fontSize = 12.sp,
                        color = Neutral700
                    )

                    Text(
                        text = "2. Contact & Identity Privacy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FutaNavyDark
                    )
                    Text(
                        text = "Phone numbers are concealed by default. Only once a roommate request is mutually accepted can students view shared contact details or exchange messages.",
                        fontSize = 12.sp,
                        color = Neutral700
                    )

                    Text(
                        text = "3. Anti-Fraud & Physical Safety",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FutaNavyDark
                    )
                    Text(
                        text = "Users must never send money, inspection fees, or rent deposits to unverified agents before physically inspecting an apartment with a peer. Suspicious listings can be reported for immediate administrative takedown.",
                        fontSize = 12.sp,
                        color = Neutral700
                    )

                    Text(
                        text = "4. Account & Data Deletion",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FutaNavyDark
                    )
                    Text(
                        text = "Students may at any time request immediate deletion of their profile, photos, accommodation listings, and conversation logs using the account deletion trigger in the profile tab.",
                        fontSize = 12.sp,
                        color = Neutral700
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close Policy")
                }
            }
        }
    }
}
