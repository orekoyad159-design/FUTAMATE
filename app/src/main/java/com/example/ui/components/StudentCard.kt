package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccommodationStatus
import com.example.data.model.Student
import com.example.data.repository.MatchResult
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StudentCard(
    student: Student,
    matchResult: MatchResult,
    onViewProfile: () -> Unit,
    onRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nairaFormatter = NumberFormat.getNumberInstance(Locale.US)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("student_card_${student.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Neutral200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Avatar, Name, Dept, Match badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Student Avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(student.avatarColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.avatarInitials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = student.fullName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        FutaVerifiedBadge(status = student.verificationStatus)
                    }

                    Text(
                        text = "${student.department} • ${student.level.shortLabel}",
                        fontSize = 12.sp,
                        color = Neutral500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Optional religion disclosure if user chose to share
                    if (student.showReligionPublicly && student.religion != null) {
                        Text(
                            text = "Faith: ${student.religion}",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }
                }

                CompatibilityBadge(percentage = matchResult.percentage)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Accommodation Status & Area & Budget Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AccommodationStatusBadge(status = student.accommodationStatus)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = FutaGoldDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = student.preferredLocation,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Neutral700
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rent / Budget highlight
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Neutral100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (student.accommodationStatus == AccommodationStatus.HAS_APARTMENT)
                            "Roommate Share: ₦${nairaFormatter.format(student.hasApartmentDetails?.individualRentNaira ?: student.budgetNaira)}/yr"
                        else
                            "Target Budget: ₦${nairaFormatter.format(student.budgetNaira)}/yr",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutaNavyDark
                    )

                    if (student.accommodationStatus == AccommodationStatus.HAS_APARTMENT) {
                        val spaces = student.hasApartmentDetails?.availableSpaces ?: 1
                        Text(
                            text = "$spaces Space${if (spaces > 1) "s" else ""} left",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FutaEmerald
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short bio
            Text(
                text = student.bio,
                fontSize = 12.sp,
                color = Neutral700,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            // Match highlights chips
            if (matchResult.highlights.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    matchResult.highlights.forEach { highlight ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = FutaGold.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "✓ $highlight",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewProfile,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("view_profile_btn_${student.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, FutaNavyDark)
                ) {
                    Text(
                        text = "View Profile",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FutaNavyDark
                    )
                }

                Button(
                    onClick = onRequestClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(44.dp)
                        .testTag("send_request_btn_${student.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FutaNavyDark,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Connect",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
