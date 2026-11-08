package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.RequestStatus
import com.example.data.model.RoommateRequest
import com.example.data.model.Student
import com.example.ui.theme.*

@Composable
fun RequestsScreen(
    incomingRequests: List<RoommateRequest>,
    sentRequests: List<RoommateRequest>,
    allStudents: List<Student>,
    messagesMap: Map<String, List<ChatMessage>>,
    onAcceptRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit,
    onCancelRequest: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0 = Requests, 1 = Messages

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("requests_screen")
    ) {
        // Tab Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Roommate Connections & Chats",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = FutaNavyDark
                )
                Text(
                    text = "Accept match requests to start private accommodation chats",
                    fontSize = 11.sp,
                    color = Neutral500
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedSubTab,
                    containerColor = Neutral100,
                    contentColor = FutaNavyDark,
                    indicator = { tabPositions ->
                        // Standard indicator
                    },
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    val pendingCount = incomingRequests.count { it.status == RequestStatus.PENDING }
                    Tab(
                        selected = selectedSubTab == 0,
                        onClick = { selectedSubTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Roommate Requests", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                if (pendingCount > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FutaGoldDark
                                    ) {
                                        Text(
                                            text = "$pendingCount",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    Tab(
                        selected = selectedSubTab == 1,
                        onClick = { selectedSubTab = 1 },
                        text = {
                            Text("Private Messages", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    )
                }
            }
        }

        // Content
        if (selectedSubTab == 0) {
            RoommateRequestsTab(
                incoming = incomingRequests,
                sent = sentRequests,
                onAccept = onAcceptRequest,
                onReject = onRejectRequest,
                onCancel = onCancelRequest,
                onOpenChat = onOpenChat
            )
        } else {
            ConversationsListTab(
                messagesMap = messagesMap,
                allStudents = allStudents,
                onOpenChat = onOpenChat
            )
        }
    }
}

@Composable
private fun RoommateRequestsTab(
    incoming: List<RoommateRequest>,
    sent: List<RoommateRequest>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
    onCancel: (String) -> Unit,
    onOpenChat: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Incoming Section
        item {
            Text(
                text = "Incoming Requests (${incoming.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (incoming.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Neutral100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No pending incoming requests",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                }
            }
        } else {
            items(incoming) { req ->
                IncomingRequestCard(
                    request = req,
                    onAccept = { onAccept(req.id) },
                    onReject = { onReject(req.id) },
                    onChat = { onOpenChat(req.senderId) }
                )
            }
        }

        // Sent Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sent Requests (${sent.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (sent.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Neutral100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You haven't sent any requests yet. Discover students in the Discover tab!",
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                }
            }
        } else {
            items(sent) { req ->
                SentRequestCard(
                    request = req,
                    onCancel = { onCancel(req.id) },
                    onChat = { onOpenChat(req.receiverId) }
                )
            }
        }
    }
}

@Composable
private fun IncomingRequestCard(
    request: RoommateRequest,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onChat: () -> Unit
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
                        text = request.senderName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = request.senderDept,
                        fontSize = 11.sp,
                        color = Neutral500
                    )
                }

                StatusPill(status = request.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Neutral100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\"${request.note}\"",
                    fontSize = 12.sp,
                    color = Neutral700,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (request.status == RequestStatus.PENDING) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Decline", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = FutaEmerald),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Accept Request", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (request.status == RequestStatus.ACCEPTED) {
                Button(
                    onClick = onChat,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat with ${request.senderName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SentRequestCard(
    request: RoommateRequest,
    onCancel: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Neutral200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "To: ${request.receiverName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                StatusPill(status = request.status)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = request.note,
                fontSize = 12.sp,
                color = Neutral700,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (request.status == RequestStatus.PENDING) {
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Cancel Request", fontSize = 11.sp, color = Neutral500)
                }
            } else if (request.status == RequestStatus.ACCEPTED) {
                Button(
                    onClick = onChat,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Open Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ConversationsListTab(
    messagesMap: Map<String, List<ChatMessage>>,
    allStudents: List<Student>,
    onOpenChat: (String) -> Unit
) {
    if (messagesMap.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Neutral500
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "No private conversations yet",
                    fontWeight = FontWeight.Bold,
                    color = Neutral700
                )
                Text(
                    text = "Accept a roommate request to begin chatting privately.",
                    fontSize = 12.sp,
                    color = Neutral500,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messagesMap.keys.toList()) { studentId ->
                val otherStudent = allStudents.find { it.id == studentId }
                val msgs = messagesMap[studentId] ?: emptyList()
                val lastMsg = msgs.lastOrNull()

                if (otherStudent != null && lastMsg != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, Neutral200),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenChat(studentId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(otherStudent.avatarColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = otherStudent.avatarInitials,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = otherStudent.fullName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = lastMsg.timestampFormatted,
                                        fontSize = 10.sp,
                                        color = Neutral500
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = lastMsg.text,
                                    fontSize = 12.sp,
                                    color = Neutral700,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
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
private fun StatusPill(status: RequestStatus) {
    val (bg, text, label) = when (status) {
        RequestStatus.PENDING -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "Pending")
        RequestStatus.ACCEPTED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "Accepted ✓")
        RequestStatus.REJECTED -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Declined")
        RequestStatus.CANCELLED -> Triple(Color(0xFFE2E8F0), Color(0xFF475569), "Cancelled")
    }

    Surface(shape = RoundedCornerShape(8.dp), color = bg) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
