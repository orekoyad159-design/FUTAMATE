package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.History
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
import com.example.data.model.RelationshipStatus
import com.example.data.model.RoommateRelationship
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoommateHistoryScreen(
    currentRoommates: List<RoommateRelationship>,
    formerRoommates: List<RoommateRelationship>,
    onTransitionStatus: (String, RelationshipStatus) -> Unit,
    onMessageRoommate: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("roommate_history_screen"),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "FUTA Roommate History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = FutaNavyDark
                        )
                        Text(
                            text = "Current & former shared living connections",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Feature explanation card
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = FutaNavyDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = FutaGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Never Lose Touch with Roommates",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Whether you're currently splitting bills or moved out last semester, FUTA Roomies keeps your mutual connections intact.",
                                fontSize = 11.sp,
                                color = Neutral300
                            )
                        }
                    }
                }
            }

            // Current Roommates Section
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = FutaEmeraldLight
                    ) {
                        Text(
                            text = "ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutaEmerald,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Current Roommates (${currentRoommates.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (currentRoommates.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Neutral100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No active roommates recorded yet",
                                fontSize = 12.sp,
                                color = Neutral500
                            )
                            Text(
                                text = "When you and another student agree to pair up, you can record a shared accommodation connection here.",
                                fontSize = 11.sp,
                                color = Neutral500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(currentRoommates) { rel ->
                    RoommateConnectionCard(
                        relationship = rel,
                        isCurrent = true,
                        onTransition = {
                            onTransitionStatus(rel.id, RelationshipStatus.FORMER_ROOMMATE)
                        },
                        onMessage = { onMessageRoommate(rel.studentBId) }
                    )
                }
            }

            // Former Roommates Section
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Neutral200
                    ) {
                        Text(
                            text = "PAST",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral700,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "Former Roommates (${formerRoommates.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (formerRoommates.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Neutral100,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No former roommates in your history",
                                fontSize = 12.sp,
                                color = Neutral500
                            )
                        }
                    }
                }
            } else {
                items(formerRoommates) { rel ->
                    RoommateConnectionCard(
                        relationship = rel,
                        isCurrent = false,
                        onTransition = {
                            onTransitionStatus(rel.id, RelationshipStatus.CURRENT_ROOMMATE)
                        },
                        onMessage = { onMessageRoommate(rel.studentBId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoommateConnectionCard(
    relationship: RoommateRelationship,
    isCurrent: Boolean,
    onTransition: () -> Unit,
    onMessage: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Neutral200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = relationship.studentBName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${relationship.studentBDept} (${relationship.studentBLevel})",
                        fontSize = 11.sp,
                        color = Neutral500
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCurrent) FutaEmeraldLight else Neutral200
                ) {
                    Text(
                        text = if (isCurrent) "Current Roommate" else "Former Roommate",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrent) Color(0xFF065F46) else Neutral700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Duration & Apartment info
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Neutral100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = FutaGoldDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = relationship.apartmentLocation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Neutral700
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Neutral500,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCurrent)
                                "Living together since ${relationship.startDate}"
                            else
                                "Shared stay: ${relationship.startDate} – ${relationship.endDate ?: "Graduation"}",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onTransition,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isCurrent) "Mark Moved Out" else "Re-establish Active",
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onMessage,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Message", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
