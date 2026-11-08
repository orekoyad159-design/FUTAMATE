package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FutaConstants
import com.example.data.model.Student
import com.example.data.repository.MatchResult
import com.example.ui.AppTab
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    currentUser: Student,
    topMatches: List<Pair<Student, MatchResult>>,
    activeListingsCount: Int,
    currentRoommatesCount: Int,
    onNavigateTab: (AppTab) -> Unit,
    onViewProfile: (String) -> Unit,
    onRequestConnect: (String) -> Unit,
    onEditAccommodation: () -> Unit,
    onLocationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Hero Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FutaNavyDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome back,",
                                fontSize = 13.sp,
                                color = FutaGold
                            )
                            Text(
                                text = currentUser.fullName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${currentUser.department} • ${currentUser.level.shortLabel}",
                                fontSize = 12.sp,
                                color = Neutral300
                            )
                        }

                        FutaVerifiedBadge(status = currentUser.verificationStatus)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Current Accommodation Status Pill inside Hero
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Status:",
                                    fontSize = 11.sp,
                                    color = Neutral300
                                )
                                Text(
                                    text = currentUser.accommodationStatus.label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            TextButton(
                                onClick = onEditAccommodation,
                                colors = ButtonDefaults.textButtonColors(contentColor = FutaGold)
                            ) {
                                Text("Update", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Stats Metric Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Compatible Matches",
                    value = "${topMatches.size}",
                    subtext = "Around FUTA",
                    icon = Icons.Default.Group,
                    iconTint = FutaNavyDark,
                    onClick = { onNavigateTab(AppTab.DISCOVER) },
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Active Apartments",
                    value = "$activeListingsCount",
                    subtext = "Open spaces",
                    icon = Icons.Default.Apartment,
                    iconTint = FutaEmerald,
                    onClick = { onNavigateTab(AppTab.LISTINGS) },
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Roommates",
                    value = "$currentRoommatesCount",
                    subtext = "Connected",
                    icon = Icons.Default.Handshake,
                    iconTint = FutaGoldDark,
                    onClick = { onNavigateTab(AppTab.PROFILE) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Safety Banner
        item {
            FutaSafetyBanner()
        }

        // Popular FUTA Areas Quick Selector
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Student Hubs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Browse by Area",
                        fontSize = 12.sp,
                        color = FutaNavyDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("South Gate", "North Gate", "Obakekere", "Roadblock", "Aule Road", "FUTA Junction")) { location ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, Neutral200),
                            modifier = Modifier.clickable {
                                onLocationSelected(location)
                                onNavigateTab(AppTab.DISCOVER)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = FutaGoldDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = location,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Top Recommended Roommates Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Recommended for You",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Based on your budget, location & study lifestyle",
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                }

                TextButton(
                    onClick = { onNavigateTab(AppTab.DISCOVER) },
                    colors = ButtonDefaults.textButtonColors(contentColor = FutaNavyDark)
                ) {
                    Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Top Roommate Cards
        if (topMatches.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Neutral100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SearchOff,
                            contentDescription = null,
                            tint = Neutral500,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No compatible roommates found",
                            fontWeight = FontWeight.Bold,
                            color = Neutral700
                        )
                        Text(
                            text = "Try adjusting your budget or preferred area in settings.",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                }
            }
        } else {
            items(topMatches.take(3)) { (student, matchResult) ->
                StudentCard(
                    student = student,
                    matchResult = matchResult,
                    onViewProfile = { onViewProfile(student.id) },
                    onRequestClick = { onRequestConnect(student.id) }
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Neutral700,
                maxLines = 1
            )
        }
    }
}
