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
import com.example.data.model.AccommodationStatus
import com.example.data.model.ApartmentDetails
import com.example.data.model.FutaConstants
import com.example.data.model.Student
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccommodationModal(
    student: Student,
    onDismiss: () -> Unit,
    onSave: (AccommodationStatus, String, Int, ApartmentDetails?) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(student.accommodationStatus) }
    var location by remember { mutableStateOf(student.preferredLocation) }
    var budgetText by remember { mutableStateOf("${student.budgetNaira}") }

    // Additional fields for Option A (Has Apartment)
    var totalRentText by remember { mutableStateOf("${student.hasApartmentDetails?.totalRentNaira ?: 280_000}") }
    var availableSpaces by remember { mutableStateOf("${student.hasApartmentDetails?.availableSpaces ?: 1}") }
    var apartmentType by remember { mutableStateOf(student.hasApartmentDetails?.apartmentType ?: "Self-Contain") }
    var description by remember { mutableStateOf(student.hasApartmentDetails?.description ?: "Shared room with borehole water and prepaid meter.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("edit_accommodation_dialog")
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
                    Text(
                        text = "Update Accommodation Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = FutaNavyDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Select your current accommodation situation at FUTA:",
                        fontSize = 12.sp,
                        color = Neutral700
                    )

                    // 3 Status Options Cards
                    AccommodationStatus.values().forEach { status ->
                        val isSelected = selectedStatus == status
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) FutaNavyDark.copy(alpha = 0.08f) else Neutral100,
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) FutaNavyDark else Neutral200
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStatus = status }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedStatus = status },
                                    colors = RadioButtonDefaults.colors(selectedColor = FutaNavyDark)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = status.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) FutaNavyDark else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = status.description,
                                        fontSize = 11.sp,
                                        color = Neutral500
                                    )
                                }
                            }
                        }
                    }

                    Divider(color = Neutral200)

                    // Preferred Location
                    Text(text = "Preferred FUTA Campus Area", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    var locationExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = locationExpanded,
                        onExpandedChange = { locationExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = locationExpanded,
                            onDismissRequest = { locationExpanded = false }
                        ) {
                            FutaConstants.LOCATIONS.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        location = loc
                                        locationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Budget Field
                    Text(
                        text = if (selectedStatus == AccommodationStatus.HAS_APARTMENT)
                            "Roommate Contribution Share (₦/year)"
                        else
                            "Target Annual Budget (₦/year)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    OutlinedTextField(
                        value = budgetText,
                        onValueChange = { budgetText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        prefix = { Text("₦ ") }
                    )

                    // Additional details if Has Apartment
                    if (selectedStatus == AccommodationStatus.HAS_APARTMENT) {
                        Text(text = "Total Flat Rent (₦/year)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = totalRentText,
                            onValueChange = { totalRentText = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            prefix = { Text("₦ ") }
                        )

                        Text(text = "Available Spaces", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = availableSpaces,
                            onValueChange = { availableSpaces = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Text(text = "Apartment Type", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = apartmentType,
                            onValueChange = { apartmentType = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            placeholder = { Text("e.g. Self-Contain or 2-Bed Flat") }
                        )

                        Text(text = "Lodge Facilities & Description", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val budget = budgetText.toIntOrNull() ?: 120_000
                        val aptDetails = if (selectedStatus == AccommodationStatus.HAS_APARTMENT) {
                            val totalRent = totalRentText.toIntOrNull() ?: (budget * 2)
                            val spaces = availableSpaces.toIntOrNull() ?: 1
                            ApartmentDetails(
                                location = location,
                                totalRentNaira = totalRent,
                                individualRentNaira = budget,
                                availableSpaces = spaces,
                                totalRooms = if (apartmentType.contains("2-Bed")) 2 else 1,
                                apartmentType = apartmentType,
                                description = description
                            )
                        } else null

                        onSave(selectedStatus, location, budget, aptDetails)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FutaNavyDark)
                ) {
                    Text("Save Accommodation Details", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
