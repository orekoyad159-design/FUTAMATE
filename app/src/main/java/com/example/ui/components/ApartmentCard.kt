package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Check
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
import com.example.data.model.ApartmentListing
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ApartmentCard(
    listing: ApartmentListing,
    onContactOwner: () -> Unit,
    onToggleOccupied: () -> Unit,
    isOwner: Boolean = false,
    modifier: Modifier = Modifier
) {
    val nairaFormatter = NumberFormat.getNumberInstance(Locale.US)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("apartment_card_${listing.id}"),
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
            // Header: Title & Availability Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = listing.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = FutaGoldDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = listing.location,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Neutral700
                        )
                        Text(text = "•", color = Neutral500)
                        Text(
                            text = listing.apartmentType,
                            fontSize = 12.sp,
                            color = Neutral500
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (listing.isOccupied) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = if (listing.isOccupied) "Occupied" else "${listing.availableSpaces} Available",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (listing.isOccupied) Color(0xFFDC2626) else Color(0xFF15803D),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rent Breakdown Box (Total vs. Individual share per PRD Feature 6)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Neutral100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Roommate Contribution",
                            fontSize = 10.sp,
                            color = Neutral500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₦${nairaFormatter.format(listing.individualRentNaira)} / year",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FutaNavyDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Neutral300)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Apartment Rent",
                            fontSize = 10.sp,
                            color = Neutral500,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₦${nairaFormatter.format(listing.totalRentNaira)} / year",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = listing.description,
                fontSize = 12.sp,
                color = Neutral700,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Amenities Chips
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listing.facilities) { facility ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = FutaEmerald,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = facility,
                                fontSize = 10.sp,
                                color = Neutral700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Owner footer & action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Listed by ${listing.ownerName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Neutral700
                    )
                    Text(
                        text = "${listing.ownerDepartment} (${listing.ownerLevel})",
                        fontSize = 10.sp,
                        color = Neutral500
                    )
                }

                if (isOwner) {
                    OutlinedButton(
                        onClick = onToggleOccupied,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = if (listing.isOccupied) "Mark Available" else "Mark Occupied",
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onContactOwner,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FutaNavyDark,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("inquire_listing_${listing.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubble,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Connect / Inquire",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
