package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApartmentListing
import com.example.data.model.FutaConstants
import com.example.ui.components.ApartmentCard
import com.example.ui.theme.*

@Composable
fun ListingsScreen(
    listings: List<ApartmentListing>,
    currentUserId: String,
    selectedLocation: String?,
    onLocationFilterChange: (String?) -> Unit,
    onContactOwner: (String) -> Unit,
    onToggleOccupied: (String) -> Unit,
    onCreateListingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("listings_screen"),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateListingClick,
                containerColor = FutaNavyDark,
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Post Apartment", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("post_apartment_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header with Location Filter
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FUTA Apartment Listings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = FutaNavyDark
                            )
                            Text(
                                text = "Direct student-listed rooms & shared flats",
                                fontSize = 11.sp,
                                color = Neutral500
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FutaGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${listings.size} Available",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutaGoldDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Location filter pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedLocation == null || selectedLocation == "All",
                                onClick = { onLocationFilterChange("All") },
                                label = { Text("All Areas", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FutaNavyDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }

                        items(FutaConstants.LOCATIONS.take(6)) { loc ->
                            FilterChip(
                                selected = selectedLocation == loc,
                                onClick = {
                                    val next = if (selectedLocation == loc) "All" else loc
                                    onLocationFilterChange(next)
                                },
                                label = { Text(loc, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FutaNavyDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Notice about rent transparency (Feature 6 requirement)
            Surface(
                color = Neutral100,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = null,
                        tint = FutaNavyMedium,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Rent transparency: Individual contribution is strictly separated from total flat rent.",
                        fontSize = 11.sp,
                        color = Neutral700
                    )
                }
            }

            // Listings List
            if (listings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = Neutral500
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No listings found in this area",
                            fontWeight = FontWeight.Bold,
                            color = Neutral700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Have an apartment around FUTA? Be the first student to post a space!",
                            fontSize = 12.sp,
                            color = Neutral500,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(listings) { listing ->
                        ApartmentCard(
                            listing = listing,
                            isOwner = listing.ownerId == currentUserId,
                            onContactOwner = { onContactOwner(listing.ownerId) },
                            onToggleOccupied = { onToggleOccupied(listing.id) }
                        )
                    }
                }
            }
        }
    }
}
