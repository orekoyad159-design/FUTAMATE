package com.example.data.model

import java.util.UUID

enum class AcademicLevel(val label: String, val shortLabel: String) {
    LEVEL_100("100 Level (Freshman)", "100L"),
    LEVEL_200("200 Level (Sophomore)", "200L"),
    LEVEL_300("300 Level (Penultimate/Mid)", "300L"),
    LEVEL_400("400 Level (Senior/IT)", "400L"),
    LEVEL_500("500 Level (Final Year)", "500L"),
    POSTGRAD("Postgraduate (PG)", "PG")
}

enum class AccommodationStatus(val label: String, val description: String) {
    HAS_APARTMENT("I Already Have an Apartment", "Have secured accommodation and need a roommate to share"),
    LOOKING_FOR_APARTMENT("I Am Looking for an Apartment", "Need to find an available space with another student"),
    LOOKING_FOR_PARTNER("Looking to Rent Together", "Want to pair up to search for and lease an apartment together")
}

enum class LifestyleHabit {
    NIGHT_OWL,
    EARLY_BIRD,
    STUDY_IN_SILENCE,
    BACKGROUND_MUSIC,
    VERY_CLEAN_DAILY,
    MODERATE_CLEANING,
    NON_SMOKER,
    GUESTS_ALLOWED_DAY_ONLY,
    RARE_GUESTS,
    COOKS_REGULARLY
}

enum class VerificationStatus {
    VERIFIED_FUTA_STUDENT,
    PENDING_VERIFICATION,
    UNVERIFIED
}

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELLED
}

enum class RelationshipStatus {
    CURRENT_ROOMMATE,
    FORMER_ROOMMATE
}

data class Student(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String,
    val email: String,
    val phone: String,
    val matricNumber: String,
    val department: String,
    val faculty: String,
    val level: AcademicLevel,
    val religion: String? = null,
    val gender: String = "Not specified",
    val bio: String,
    val avatarInitials: String,
    val avatarColorHex: Long = 0xFF0D3B66,
    val verificationStatus: VerificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT,
    val showPhonePublicly: Boolean = false,
    val showReligionPublicly: Boolean = false,
    val habits: List<LifestyleHabit> = emptyList(),
    val accommodationStatus: AccommodationStatus = AccommodationStatus.LOOKING_FOR_APARTMENT,
    // Accommodation preferences / details
    val preferredLocation: String = "South Gate",
    val budgetNaira: Int = 120_000,
    val hasApartmentDetails: ApartmentDetails? = null
)

data class ApartmentDetails(
    val location: String,
    val totalRentNaira: Int,
    val individualRentNaira: Int,
    val availableSpaces: Int,
    val totalRooms: Int,
    val apartmentType: String = "Self-Contain",
    val facilities: List<String> = listOf("Borehole Water", "Fenced & Gated", "Prepaid Meter"),
    val description: String = "",
    val moveInDate: String = "Immediate"
)

data class ApartmentListing(
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val ownerName: String,
    val ownerDepartment: String,
    val ownerLevel: String,
    val title: String,
    val location: String,
    val totalRentNaira: Int,
    val individualRentNaira: Int,
    val availableSpaces: Int,
    val totalRooms: Int,
    val apartmentType: String,
    val facilities: List<String>,
    val description: String,
    val moveInDate: String,
    val isOccupied: Boolean = false,
    val isVerifiedApartment: Boolean = true,
    val createdAtFormatted: String = "Today"
)

data class RoommateRequest(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String,
    val senderName: String,
    val senderDept: String,
    val receiverId: String,
    val receiverName: String,
    val status: RequestStatus = RequestStatus.PENDING,
    val note: String = "Hi! I would love to connect as potential roommates at FUTA.",
    val createdAtFormatted: String = "Just now"
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val senderId: String,
    val text: String,
    val timestampFormatted: String,
    val isRead: Boolean = true,
    val attachmentSnippet: String? = null
)

data class RoommateRelationship(
    val id: String = UUID.randomUUID().toString(),
    val studentAId: String,
    val studentBId: String,
    val studentBName: String,
    val studentBDept: String,
    val studentBLevel: String,
    val apartmentLocation: String,
    val status: RelationshipStatus,
    val startDate: String,
    val endDate: String? = null,
    val sharedNotes: String = "Shared 2-bedroom flat at South Gate with clean habits."
)

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: String = "REQUEST"
)

data class AdminReport(
    val id: String = UUID.randomUUID().toString(),
    val reporterName: String,
    val reportedEntityName: String,
    val reason: String,
    val status: String = "Pending Review",
    val dateFormatted: String = "Today"
)
