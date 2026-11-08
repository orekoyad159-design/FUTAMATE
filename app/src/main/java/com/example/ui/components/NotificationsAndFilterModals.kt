package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
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
import com.example.data.model.*
import com.example.ui.DiscoverFilters
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun NotificationsDialog(
    notifications: List<AppNotification>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .testTag("notifications_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = FutaNavyDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = FutaNavyDark
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No notifications right now",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notifications) { notif ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (notif.isRead) Neutral100 else FutaGold.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, Neutral200),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (notif.isRead) Color.Transparent else FutaGoldDark)
                                            .padding(top = 4.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = notif.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = notif.timeAgo,
                                                fontSize = 10.sp,
                                                color = Neutral500
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = notif.message,
                                            fontSize = 11.sp,
                                            color = Neutral700
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserSwitcherDialog(
    students: List<Student>,
    currentUserId: String,
    onSelectUser: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .testTag("user_switcher_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Switch Student Persona",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = FutaNavyDark
                        )
                        Text(
                            text = "Test matching, requests, & chats as different students",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(students) { student ->
                        val isCurrent = student.id == currentUserId
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) FutaNavyDark.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                if (isCurrent) 2.dp else 1.dp,
                                if (isCurrent) FutaNavyDark else Neutral200
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectUser(student.id)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(student.avatarColorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = student.avatarInitials,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = student.fullName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        if (isCurrent) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "(Active)",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = FutaEmerald
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${student.department} (${student.level.shortLabel})",
                                        fontSize = 11.sp,
                                        color = Neutral500
                                    )
                                    Text(
                                        text = student.accommodationStatus.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = FutaGoldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    filters: DiscoverFilters,
    onApply: (DiscoverFilters) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDept by remember { mutableStateOf(filters.department) }
    var selectedLevel by remember { mutableStateOf(filters.level) }
    var selectedLocation by remember { mutableStateOf(filters.location) }
    var selectedStatus by remember { mutableStateOf(filters.accommodationStatus) }
    var maxBudget by remember { mutableStateOf(filters.maxBudget.toFloat()) }
    var religionPref by remember { mutableStateOf(filters.religionFilter) }
    val nairaFormatter = NumberFormat.getNumberInstance(Locale.US)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("filter_sheet_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter Roommates",
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Location filter
                    Text(text = "Campus Area", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    var locExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = locExpanded,
                        onExpandedChange = { locExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedLocation ?: "All Locations",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = locExpanded,
                            onDismissRequest = { locExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Locations") },
                                onClick = {
                                    selectedLocation = null
                                    locExpanded = false
                                }
                            )
                            FutaConstants.LOCATIONS.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        selectedLocation = loc
                                        locExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Level Filter
                    Text(text = "Academic Level", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(null, AcademicLevel.LEVEL_100, AcademicLevel.LEVEL_200, AcademicLevel.LEVEL_300, AcademicLevel.LEVEL_400, AcademicLevel.LEVEL_500).forEach { lvl ->
                            val isSelected = selectedLevel == lvl
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedLevel = lvl },
                                label = { Text(lvl?.shortLabel ?: "All", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FutaNavyDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Max Budget Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Maximum Budget", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(
                            text = "₦${nairaFormatter.format(maxBudget.toInt())}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutaEmerald
                        )
                    }

                    Slider(
                        value = maxBudget,
                        onValueChange = { maxBudget = it },
                        valueRange = 80_000f..300_000f,
                        steps = 11,
                        colors = SliderDefaults.colors(
                            thumbColor = FutaNavyDark,
                            activeTrackColor = FutaNavyDark
                        )
                    )

                    // Optional Religion Preference (PRD Feature 4: strictly optional)
                    Text(text = "Optional Religion Match (Optional)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(null to "Any", "Christianity" to "Christian", "Islam" to "Muslim").forEach { (rel, label) ->
                            FilterChip(
                                selected = religionPref == rel,
                                onClick = { religionPref = rel },
                                label = { Text(label, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FutaGoldDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onReset()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset")
                    }

                    Button(
                        onClick = {
                            onApply(
                                filters.copy(
                                    department = selectedDept,
                                    level = selectedLevel,
                                    location = selectedLocation,
                                    accommodationStatus = selectedStatus,
                                    maxBudget = maxBudget.toInt(),
                                    religionFilter = religionPref
                                )
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
                    ) {
                        Text("Apply Filters", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
