package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FutaConstants
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingModal(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        location: String,
        totalRent: Int,
        individualRent: Int,
        availableSpaces: Int,
        totalRooms: Int,
        apartmentType: String,
        facilities: List<String>,
        description: String,
        moveInDate: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("South Gate") }
    var totalRentText by remember { mutableStateOf("260000") }
    var individualRentText by remember { mutableStateOf("130000") }
    var spacesText by remember { mutableStateOf("1") }
    var roomsText by remember { mutableStateOf("1") }
    var apartmentType by remember { mutableStateOf("Self-Contain Flat") }
    var description by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("Next Semester / Immediate") }
    val selectedFacilities = remember { mutableStateListOf("Borehole Water", "Prepaid Meter", "Fenced & Security Guard") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .testTag("create_listing_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Post FUTA Apartment",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = FutaNavyDark
                        )
                        Text(
                            text = "List a room or shared space for student roommates",
                            fontSize = 11.sp,
                            color = Neutral500
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Listing Title", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. Clean Self-Contain Lodge near South Gate") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Text(text = "Campus Location", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    var locExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = locExpanded,
                        onExpandedChange = { locExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = location,
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
                            FutaConstants.LOCATIONS.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        location = loc
                                        locExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Rent Split Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Roommate Share (₦)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            OutlinedTextField(
                                value = individualRentText,
                                onValueChange = { individualRentText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                prefix = { Text("₦") }
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Total Rent (₦)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            OutlinedTextField(
                                value = totalRentText,
                                onValueChange = { totalRentText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                prefix = { Text("₦") }
                            )
                        }
                    }

                    // Spaces & Type Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Available Spaces", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            OutlinedTextField(
                                value = spacesText,
                                onValueChange = { spacesText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1.5f)) {
                            Text(text = "Apartment Type", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            OutlinedTextField(
                                value = apartmentType,
                                onValueChange = { apartmentType = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    // Facilities selection
                    Text(text = "Lodge Facilities", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            "Borehole Water",
                            "Prepaid Meter",
                            "Fenced & Security Guard",
                            "Generator Allowed",
                            "En-suite Bathroom",
                            "Modern Tiled Floor"
                        ).forEach { fac ->
                            val checked = selectedFacilities.contains(fac)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (checked) selectedFacilities.remove(fac)
                                        else selectedFacilities.add(fac)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        if (it) selectedFacilities.add(fac)
                                        else selectedFacilities.remove(fac)
                                    }
                                )
                                Text(text = fac, fontSize = 12.sp, color = Neutral700)
                            }
                        }
                    }

                    Text(text = "Description & Preferences", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Tell students about the environment, water, light and rules...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val totalRent = totalRentText.toIntOrNull() ?: 260_000
                        val individualRent = individualRentText.toIntOrNull() ?: 130_000
                        val spaces = spacesText.toIntOrNull() ?: 1
                        val rooms = roomsText.toIntOrNull() ?: 1
                        val listingTitle = title.ifBlank { "Shared Apartment at $location" }
                        val desc = description.ifBlank { "Standard student accommodation in a serene study environment." }

                        onSubmit(
                            listingTitle,
                            location,
                            totalRent,
                            individualRent,
                            spaces,
                            rooms,
                            apartmentType,
                            selectedFacilities.toList(),
                            desc,
                            moveInDate
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
                ) {
                    Text("Publish Apartment Listing", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
