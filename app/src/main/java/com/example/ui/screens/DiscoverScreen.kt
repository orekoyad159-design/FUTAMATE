package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicLevel
import com.example.data.model.AccommodationStatus
import com.example.data.model.FutaConstants
import com.example.data.model.Student
import com.example.data.repository.MatchResult
import com.example.ui.DiscoverFilters
import com.example.ui.components.StudentCard
import com.example.ui.theme.*

@Composable
fun DiscoverScreen(
    matches: List<Pair<Student, MatchResult>>,
    filters: DiscoverFilters,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (DiscoverFilters) -> Unit,
    onResetFilters: () -> Unit,
    onOpenFilterSheet: () -> Unit,
    onViewProfile: (String) -> Unit,
    onRequestConnect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("discover_screen")
    ) {
        // Search bar & Filter button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = filters.query,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_roommates_input"),
                        placeholder = { Text("Search by name, dept, area...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Neutral500
                            )
                        },
                        trailingIcon = {
                            if (filters.query.isNotBlank()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FutaNavyDark,
                            unfocusedBorderColor = Neutral300
                        )
                    )

                    FilledIconButton(
                        onClick = onOpenFilterSheet,
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (filters.location != null || filters.level != null || filters.accommodationStatus != null) FutaGoldDark else FutaNavyDark
                        ),
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("filter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = "Filters",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Status Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = filters.accommodationStatus == null,
                            onClick = { onFilterChange(filters.copy(accommodationStatus = null)) },
                            label = { Text("All Statuses", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FutaNavyDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = filters.accommodationStatus == AccommodationStatus.HAS_APARTMENT,
                            onClick = {
                                val next = if (filters.accommodationStatus == AccommodationStatus.HAS_APARTMENT) null else AccommodationStatus.HAS_APARTMENT
                                onFilterChange(filters.copy(accommodationStatus = next))
                            },
                            label = { Text("Has Apartment", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FutaEmerald,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = filters.accommodationStatus == AccommodationStatus.LOOKING_FOR_APARTMENT,
                            onClick = {
                                val next = if (filters.accommodationStatus == AccommodationStatus.LOOKING_FOR_APARTMENT) null else AccommodationStatus.LOOKING_FOR_APARTMENT
                                onFilterChange(filters.copy(accommodationStatus = next))
                            },
                            label = { Text("Needs Room", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FutaSky,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = filters.accommodationStatus == AccommodationStatus.LOOKING_FOR_PARTNER,
                            onClick = {
                                val next = if (filters.accommodationStatus == AccommodationStatus.LOOKING_FOR_PARTNER) null else AccommodationStatus.LOOKING_FOR_PARTNER
                                onFilterChange(filters.copy(accommodationStatus = next))
                            },
                            label = { Text("Co-Rent Partner", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FutaPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Active filter indicator if any
        val hasActiveFilters = filters.location != null || filters.level != null || filters.department != null || filters.religionFilter != null
        if (hasActiveFilters) {
            Surface(
                color = FutaGold.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtered by: ${listOfNotNull(filters.location, filters.level?.shortLabel, filters.department?.take(15)).joinToString(", ")}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF78350F)
                    )
                    Text(
                        text = "Reset",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutaNavyDark,
                        modifier = Modifier.clickable { onResetFilters() }
                    )
                }
            }
        }

        // Results count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${matches.size} Potential Roommates",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Ranked by Compatibility",
                fontSize = 11.sp,
                color = Neutral500
            )
        }

        // List of Matches
        if (matches.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PersonSearch,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = Neutral500
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No FUTA roommates match your filters",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Neutral700
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try clearing filters to see students across all departments and locations.",
                        fontSize = 12.sp,
                        color = Neutral500,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onResetFilters,
                        colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
                    ) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(matches) { (student, matchResult) ->
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
}
