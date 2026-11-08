package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Handshake
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
import com.example.data.model.ChatMessage
import com.example.data.model.Student
import com.example.ui.components.FutaVerifiedBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    currentUserId: String,
    recipient: Student,
    messages: List<ChatMessage>,
    onSendMessage: (String, String?) -> Unit,
    onEstablishRoommate: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var textInput by remember { mutableStateOf("") }
    var selectedAttachment by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("chat_screen"),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(recipient.avatarColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = recipient.avatarInitials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = recipient.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                FutaVerifiedBadge(status = recipient.verificationStatus)
                            }
                            Text(
                                text = "${recipient.department} (${recipient.level.shortLabel})",
                                fontSize = 11.sp,
                                color = Neutral500
                            )
                        }
                    }
                },
                actions = {
                    // Feature 9 Action: Establish Roommate
                    TextButton(
                        onClick = onEstablishRoommate,
                        colors = ButtonDefaults.textButtonColors(contentColor = FutaEmerald)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Handshake,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pair as Roommate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Safety Header in Chat
            Surface(
                color = Color(0xFFFEF3C7),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🔒 Safe Chat: Agree on rent shares and house rules before making any commitments.",
                    fontSize = 10.sp,
                    color = Color(0xFF92400E),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            // Messages Stream
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    val isMe = message.senderId == currentUserId

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp,
                                bottomStart = if (isMe) 14.dp else 2.dp,
                                bottomEnd = if (isMe) 2.dp else 14.dp
                            ),
                            color = if (isMe) FutaNavyDark else MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                if (message.attachmentSnippet != null) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isMe) Color.White.copy(alpha = 0.15f) else Neutral200,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Attachment,
                                                contentDescription = null,
                                                tint = if (isMe) FutaGold else FutaNavyDark,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = message.attachmentSnippet,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (isMe) Color.White else Neutral700
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = message.text,
                                    fontSize = 13.sp,
                                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(
                                        text = message.timestampFormatted,
                                        fontSize = 9.sp,
                                        color = if (isMe) Neutral300 else Neutral500
                                    )
                                    if (isMe) {
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Icon(
                                            imageVector = Icons.Default.DoneAll,
                                            contentDescription = "Read",
                                            tint = FutaGold,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Topic Attachment Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val attachments = listOf(
                    "Apartment Specs (${recipient.preferredLocation})",
                    "Utility Bill Split: 50/50",
                    "House Rules: Quiet Study Hours",
                    "Move-in Date: Start of Semester"
                )

                items(attachments) { attach ->
                    val isSelected = selectedAttachment == attach
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) FutaGoldDark else Neutral100,
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    ) {
                        TextButton(
                            onClick = {
                                selectedAttachment = if (isSelected) null else attach
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = if (isSelected) "✓ $attach" else "+ $attach",
                                fontSize = 10.sp,
                                color = if (isSelected) Color.White else Neutral700
                            )
                        }
                    }
                }
            }

            // Message Input Row
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        placeholder = {
                            Text(
                                text = if (selectedAttachment != null) "Add comment on: $selectedAttachment" else "Type your message to ${recipient.fullName.split(" ").first()}...",
                                fontSize = 12.sp
                            )
                        },
                        maxLines = 3,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FutaNavyDark,
                            unfocusedBorderColor = Neutral300
                        )
                    )

                    FilledIconButton(
                        onClick = {
                            if (textInput.isNotBlank() || selectedAttachment != null) {
                                val messageText = textInput.ifBlank { "Sent shared details:" }
                                onSendMessage(messageText, selectedAttachment)
                                textInput = ""
                                selectedAttachment = null
                            }
                        },
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = FutaNavyDark),
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
