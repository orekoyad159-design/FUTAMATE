package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Shield
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
import com.example.data.model.AccommodationStatus
import com.example.data.model.LifestyleHabit
import com.example.data.model.Student
import com.example.data.repository.MatchResult
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StudentDetailModal(
    student: Student,
    matchResult: MatchResult,
    onDismiss: () -> Unit,
    onSendRequest: (String) -> Unit
) {
    var showRequestNoteDialog by remember { mutableStateOf(false) }
    val nairaFormatter = NumberFormat.getNumberInstance(Locale.US)

    if (showRequestNoteDialog) {
        SendRequestDialog(
            recipientName = student.fullName,
            onDismiss = { showRequestNoteDialog = false },
            onSend = { note ->
                showRequestNoteDialog = false
                onSendRequest(note)
                onDismiss()
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("student_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top close bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompatibilityBadge(percentage = matchResult.percentage)

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(student.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.avatarInitials,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = student.fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        FutaVerifiedBadge(status = student.verificationStatus)
                    }

                    Text(
                        text = "${student.department} (${student.level.label})",
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                    Text(
                        text = student.faculty,
                        fontSize = 11.sp,
                        color = Neutral500
                    )

                    if (student.showReligionPublicly && student.religion != null) {
                        Text(
                            text = "Religion: ${student.religion}",
                            fontSize = 11.sp,
                            color = Neutral700
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Accommodation Situation
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Neutral100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Accommodation Situation",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                AccommodationStatusBadge(status = student.accommodationStatus)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Preferred Location: ${student.preferredLocation}",
                                fontSize = 12.sp,
                                color = Neutral700
                            )

                            Text(
                                text = if (student.accommodationStatus == AccommodationStatus.HAS_APARTMENT)
                                    "Individual Roommate Share: ₦${nairaFormatter.format(student.hasApartmentDetails?.individualRentNaira ?: student.budgetNaira)}/yr"
                                else
                                    "Individual Budget: ₦${nairaFormatter.format(student.budgetNaira)}/yr",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutaNavyDark
                            )

                            if (student.hasApartmentDetails != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Apartment: ${student.hasApartmentDetails.apartmentType} (${student.hasApartmentDetails.availableSpaces} space available)",
                                    fontSize = 11.sp,
                                    color = Neutral700
                                )
                                Text(
                                    text = "Facilities: ${student.hasApartmentDetails.facilities.joinToString(", ")}",
                                    fontSize = 11.sp,
                                    color = Neutral500
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // About Me
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "About Me",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = student.bio,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = Neutral700
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lifestyle Habits
                    if (student.habits.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Lifestyle & Study Habits",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            student.habits.forEach { habit ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = FutaEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = habit.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                                        fontSize = 11.sp,
                                        color = Neutral700
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Compatibility Breakdown
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = FutaGold.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Compatibility Highlights (${matchResult.percentage}%)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            matchResult.highlights.forEach { hl ->
                                Text(
                                    text = "• $hl",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Connect Action
                Button(
                    onClick = { showRequestNoteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("modal_connect_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send Roommate Request", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SendRequestDialog(
    recipientName: String,
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var note by remember { mutableStateOf("Hi $recipientName! I saw your profile on FUTA Roomies and our budget and location preferences match. Would love to share accommodation.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Connect with $recipientName",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Add a personal note to introduce yourself and your accommodation plans:",
                    fontSize = 12.sp,
                    color = Neutral700
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(10.dp),
                    placeholder = { Text("Write your message...") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(note) },
                colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
            ) {
                Text("Send Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
