package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class MatchResult(
    val percentage: Int,
    val highlights: List<String>
)

class FutaRepository {

    private val _allStudents = MutableStateFlow<List<Student>>(FutaConstants.getInitialStudents())
    val allStudents: StateFlow<List<Student>> = _allStudents.asStateFlow()

    private val _currentUserId = MutableStateFlow("student_tunde")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _listings = MutableStateFlow<List<ApartmentListing>>(FutaConstants.getInitialListings())
    val listings: StateFlow<List<ApartmentListing>> = _listings.asStateFlow()

    private val _requests = MutableStateFlow<List<RoommateRequest>>(
        listOf(
            RoommateRequest(
                id = "req_initial_1",
                senderId = "student_aminat",
                senderName = "Aminat Bello",
                senderDept = "Biochemistry (200L)",
                receiverId = "student_tunde",
                receiverName = "Tunde Adeleke",
                status = RequestStatus.PENDING,
                note = "Hello Tunde, noticed your quiet study habits and SEET focus. Would you be open to pairing up for a flat around South Gate?",
                createdAtFormatted = "2 hours ago"
            ),
            RoommateRequest(
                id = "req_initial_2",
                senderId = "student_tunde",
                senderName = "Tunde Adeleke",
                senderDept = "Industrial & Production Eng (200L)",
                receiverId = "student_chioma",
                receiverName = "Chioma Nwosu",
                status = RequestStatus.PENDING,
                note = "Hi Chioma, saw your South Gate self-contain listing. Very interested in splitting the rent for next session.",
                createdAtFormatted = "Yesterday"
            )
        )
    )
    val requests: StateFlow<List<RoommateRequest>> = _requests.asStateFlow()

    private val _relationships = MutableStateFlow<List<RoommateRelationship>>(
        listOf(
            RoommateRelationship(
                id = "rel_1",
                studentAId = "student_tunde",
                studentBId = "student_segun",
                studentBName = "Segun Bakare",
                studentBDept = "Architecture",
                studentBLevel = "400L",
                apartmentLocation = "South Gate Lodge (Room 4B)",
                status = RelationshipStatus.FORMER_ROOMMATE,
                startDate = "Nov 2023",
                endDate = "Oct 2024",
                sharedNotes = "Shared a peaceful 2-room flat during 100L. Very clean and respectful roommate."
            ),
            RoommateRelationship(
                id = "rel_2",
                studentAId = "student_tunde",
                studentBId = "student_kazeem",
                studentBName = "Kazeem Oladipo",
                studentBDept = "Mechanical Engineering",
                studentBLevel = "200L",
                apartmentLocation = "Roadblock Court",
                status = RelationshipStatus.CURRENT_ROOMMATE,
                startDate = "Jan 2025",
                endDate = null,
                sharedNotes = "Currently sharing utility bills and rent at Roadblock Court."
            )
        )
    )
    val relationships: StateFlow<List<RoommateRelationship>> = _relationships.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "student_kazeem" to listOf(
                ChatMessage(
                    id = "msg_1",
                    conversationId = "student_kazeem",
                    senderId = "student_kazeem",
                    text = "Hey Tunde! Just reminding you that the NEPA/prepaid unit was recharged today.",
                    timestampFormatted = "10:15 AM",
                    isRead = true
                ),
                ChatMessage(
                    id = "msg_2",
                    conversationId = "student_kazeem",
                    senderId = "student_tunde",
                    text = "Great thanks bro! I will send my ₦3,000 share for this month now.",
                    timestampFormatted = "10:20 AM",
                    isRead = true
                )
            ),
            "student_chioma" to listOf(
                ChatMessage(
                    id = "msg_3",
                    conversationId = "student_chioma",
                    senderId = "student_tunde",
                    text = "Hi Chioma, is the available space at your South Gate lodge still open?",
                    timestampFormatted = "Yesterday 4:30 PM",
                    isRead = true
                ),
                ChatMessage(
                    id = "msg_4",
                    conversationId = "student_chioma",
                    senderId = "student_chioma",
                    text = "Yes Tunde! You can inspect it anytime after my lectures at SOC building.",
                    timestampFormatted = "Yesterday 5:10 PM",
                    isRead = true,
                    attachmentSnippet = "Attached: South Gate Self-Contain (₦140,000/yr)"
                )
            )
        )
    )
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                id = "notif_1",
                userId = "student_tunde",
                title = "New Roommate Request",
                message = "Aminat Bello sent you a roommate request for South Gate.",
                timeAgo = "2h ago",
                isRead = false,
                type = "REQUEST"
            ),
            AppNotification(
                id = "notif_2",
                userId = "student_tunde",
                title = "FUTA Student Verified",
                message = "Your matriculation number IPE/22/4512 has been successfully verified.",
                timeAgo = "1d ago",
                isRead = true,
                type = "VERIFY"
            ),
            AppNotification(
                id = "notif_3",
                userId = "student_tunde",
                title = "Roommate Connection",
                message = "Kazeem Oladipo confirmed your Current Roommate relationship.",
                timeAgo = "3d ago",
                isRead = true,
                type = "CONNECTION"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _reports = MutableStateFlow<List<AdminReport>>(
        listOf(
            AdminReport(
                id = "rep_1",
                reporterName = "Anonymous Student",
                reportedEntityName = "Fake Lodge Agent at Obanla",
                reason = "Demanding ₦20,000 upfront inspection fee before showing room.",
                status = "Flagged & Blocked",
                dateFormatted = "Yesterday"
            )
        )
    )
    val reports: StateFlow<List<AdminReport>> = _reports.asStateFlow()

    fun getCurrentUser(): Student {
        val currentId = _currentUserId.value
        return _allStudents.value.find { it.id == currentId } ?: _allStudents.value.first()
    }

    fun switchActiveUser(studentId: String) {
        if (_allStudents.value.any { it.id == studentId }) {
            _currentUserId.value = studentId
        }
    }

    fun updateStudentProfile(updatedStudent: Student) {
        _allStudents.value = _allStudents.value.map {
            if (it.id == updatedStudent.id) updatedStudent else it
        }
    }

    fun updateAccommodationStatus(
        status: AccommodationStatus,
        preferredLocation: String,
        budget: Int,
        apartmentDetails: ApartmentDetails? = null
    ) {
        val current = getCurrentUser()
        val updated = current.copy(
            accommodationStatus = status,
            preferredLocation = preferredLocation,
            budgetNaira = budget,
            hasApartmentDetails = if (status == AccommodationStatus.HAS_APARTMENT) apartmentDetails else null
        )
        updateStudentProfile(updated)

        // If the user has an apartment, create/update corresponding listing
        if (status == AccommodationStatus.HAS_APARTMENT && apartmentDetails != null) {
            val existing = _listings.value.find { it.ownerId == current.id }
            if (existing != null) {
                _listings.value = _listings.value.map {
                    if (it.id == existing.id) {
                        it.copy(
                            location = apartmentDetails.location,
                            totalRentNaira = apartmentDetails.totalRentNaira,
                            individualRentNaira = apartmentDetails.individualRentNaira,
                            availableSpaces = apartmentDetails.availableSpaces,
                            facilities = apartmentDetails.facilities,
                            description = apartmentDetails.description
                        )
                    } else it
                }
            } else {
                createListing(
                    title = "${current.fullName}'s Shared Flat at ${apartmentDetails.location}",
                    location = apartmentDetails.location,
                    totalRent = apartmentDetails.totalRentNaira,
                    individualRent = apartmentDetails.individualRentNaira,
                    availableSpaces = apartmentDetails.availableSpaces,
                    totalRooms = apartmentDetails.totalRooms,
                    apartmentType = apartmentDetails.apartmentType,
                    facilities = apartmentDetails.facilities,
                    description = apartmentDetails.description,
                    moveInDate = apartmentDetails.moveInDate
                )
            }
        }
    }

    fun sendRoommateRequest(receiverId: String, note: String): Boolean {
        val current = getCurrentUser()
        val receiver = _allStudents.value.find { it.id == receiverId } ?: return false

        // Prevent duplicate pending requests
        val exists = _requests.value.any {
            (it.senderId == current.id && it.receiverId == receiverId && it.status == RequestStatus.PENDING) ||
            (it.senderId == receiverId && it.receiverId == current.id && it.status == RequestStatus.PENDING)
        }
        if (exists) return false

        val newRequest = RoommateRequest(
            id = UUID.randomUUID().toString(),
            senderId = current.id,
            senderName = current.fullName,
            senderDept = "${current.department} (${current.level.shortLabel})",
            receiverId = receiver.id,
            receiverName = receiver.fullName,
            status = RequestStatus.PENDING,
            note = note.ifBlank { "Hi! I would love to connect as roommates at FUTA." },
            createdAtFormatted = "Just now"
        )
        _requests.value = listOf(newRequest) + _requests.value

        // Add notification for receiver
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            userId = receiver.id,
            title = "New Roommate Request",
            message = "${current.fullName} sent you a roommate request.",
            timeAgo = "Just now",
            isRead = false,
            type = "REQUEST"
        )
        _notifications.value = listOf(notif) + _notifications.value
        return true
    }

    fun acceptRoommateRequest(requestId: String) {
        val req = _requests.value.find { it.id == requestId } ?: return
        _requests.value = _requests.value.map {
            if (it.id == requestId) it.copy(status = RequestStatus.ACCEPTED) else it
        }

        // Add notification to sender
        val senderNotif = AppNotification(
            id = UUID.randomUUID().toString(),
            userId = req.senderId,
            title = "Request Accepted! 🎉",
            message = "${req.receiverName} accepted your roommate request. You can now chat privately.",
            timeAgo = "Just now",
            isRead = false,
            type = "ACCEPTED"
        )
        _notifications.value = listOf(senderNotif) + _notifications.value

        // Seed initial greeting message
        val otherPersonId = if (getCurrentUser().id == req.receiverId) req.senderId else req.receiverId
        val existingMsgs = _messages.value[otherPersonId] ?: emptyList()
        if (existingMsgs.isEmpty()) {
            val welcomeMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = otherPersonId,
                senderId = req.receiverId,
                text = "Hi ${req.senderName}! I accepted your roommate request. Let's discuss our accommodation plans!",
                timestampFormatted = "Just now",
                isRead = false
            )
            val updatedMap = _messages.value.toMutableMap()
            updatedMap[otherPersonId] = listOf(welcomeMsg)
            _messages.value = updatedMap
        }
    }

    fun rejectRoommateRequest(requestId: String) {
        _requests.value = _requests.value.map {
            if (it.id == requestId) it.copy(status = RequestStatus.REJECTED) else it
        }
    }

    fun cancelRoommateRequest(requestId: String) {
        _requests.value = _requests.value.map {
            if (it.id == requestId) it.copy(status = RequestStatus.CANCELLED) else it
        }
    }

    fun sendMessage(recipientId: String, text: String, attachment: String? = null) {
        val current = getCurrentUser()
        val timeNow = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            conversationId = recipientId,
            senderId = current.id,
            text = text,
            timestampFormatted = timeNow,
            isRead = false,
            attachmentSnippet = attachment
        )

        val updatedMap = _messages.value.toMutableMap()
        val currentList = updatedMap[recipientId] ?: emptyList()
        updatedMap[recipientId] = currentList + newMsg
        _messages.value = updatedMap
    }

    fun createListing(
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
    ) {
        val current = getCurrentUser()
        val newListing = ApartmentListing(
            id = UUID.randomUUID().toString(),
            ownerId = current.id,
            ownerName = current.fullName,
            ownerDepartment = current.department,
            ownerLevel = current.level.shortLabel,
            title = title,
            location = location,
            totalRentNaira = totalRent,
            individualRentNaira = individualRent,
            availableSpaces = availableSpaces,
            totalRooms = totalRooms,
            apartmentType = apartmentType,
            facilities = facilities,
            description = description,
            moveInDate = moveInDate,
            isOccupied = false,
            isVerifiedApartment = true,
            createdAtFormatted = "Just now"
        )
        _listings.value = listOf(newListing) + _listings.value
    }

    fun toggleListingOccupied(listingId: String) {
        _listings.value = _listings.value.map {
            if (it.id == listingId) it.copy(isOccupied = !it.isOccupied) else it
        }
    }

    fun establishRoommateConnection(targetStudentId: String, location: String) {
        val current = getCurrentUser()
        val target = _allStudents.value.find { it.id == targetStudentId } ?: return
        val currentMonthYear = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())

        val newRel = RoommateRelationship(
            id = UUID.randomUUID().toString(),
            studentAId = current.id,
            studentBId = target.id,
            studentBName = target.fullName,
            studentBDept = target.department,
            studentBLevel = target.level.shortLabel,
            apartmentLocation = location,
            status = RelationshipStatus.CURRENT_ROOMMATE,
            startDate = currentMonthYear,
            endDate = null,
            sharedNotes = "Confirmed shared accommodation at $location."
        )
        _relationships.value = listOf(newRel) + _relationships.value

        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            title = "Roommate Established! 🏠",
            message = "You and ${target.fullName} are now recorded as Current Roommates at $location.",
            timeAgo = "Just now",
            isRead = false,
            type = "CONNECTION"
        )
        _notifications.value = listOf(notif) + _notifications.value
    }

    fun transitionRoommateStatus(relationshipId: String, newStatus: RelationshipStatus) {
        val currentMonthYear = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
        _relationships.value = _relationships.value.map {
            if (it.id == relationshipId) {
                it.copy(
                    status = newStatus,
                    endDate = if (newStatus == RelationshipStatus.FORMER_ROOMMATE) currentMonthYear else null
                )
            } else it
        }
    }

    fun verifyStudent(studentId: String) {
        _allStudents.value = _allStudents.value.map {
            if (it.id == studentId) {
                it.copy(verificationStatus = VerificationStatus.VERIFIED_FUTA_STUDENT)
            } else it
        }
    }

    fun reportEntity(reportedName: String, reason: String) {
        val current = getCurrentUser()
        val newReport = AdminReport(
            id = UUID.randomUUID().toString(),
            reporterName = current.fullName,
            reportedEntityName = reportedName,
            reason = reason,
            status = "Pending Review",
            dateFormatted = "Just now"
        )
        _reports.value = listOf(newReport) + _reports.value
    }

    fun markNotificationsRead() {
        val currentId = _currentUserId.value
        _notifications.value = _notifications.value.map {
            if (it.userId == currentId) it.copy(isRead = true) else it
        }
    }

    fun calculateCompatibility(studentA: Student, studentB: Student): MatchResult {
        var score = 40
        val highlights = mutableListOf<String>()

        // 1. Accommodation Status synergy (up to 30 pts)
        val hasA = studentA.accommodationStatus == AccommodationStatus.HAS_APARTMENT
        val hasB = studentB.accommodationStatus == AccommodationStatus.HAS_APARTMENT
        val lookingA = studentA.accommodationStatus == AccommodationStatus.LOOKING_FOR_APARTMENT
        val lookingB = studentB.accommodationStatus == AccommodationStatus.LOOKING_FOR_APARTMENT
        val partnerA = studentA.accommodationStatus == AccommodationStatus.LOOKING_FOR_PARTNER
        val partnerB = studentB.accommodationStatus == AccommodationStatus.LOOKING_FOR_PARTNER

        if ((hasA && lookingB) || (hasB && lookingA)) {
            score += 28
            highlights.add("Apartment Match (Space Available)")
        } else if (partnerA && partnerB) {
            score += 25
            highlights.add("Both Looking to Co-Rent")
        } else if (lookingA && lookingB) {
            score += 20
            highlights.add("Both Searching for Accommodation")
        }

        // 2. Location compatibility (up to 20 pts)
        if (studentA.preferredLocation.equals(studentB.preferredLocation, ignoreCase = true)) {
            score += 20
            highlights.add("Same Area: ${studentA.preferredLocation}")
        }

        // 3. Budget overlap (up to 20 pts)
        val budgetDiff = abs(studentA.budgetNaira - studentB.budgetNaira)
        if (budgetDiff <= 20_000) {
            score += 20
            highlights.add("Budget Compatible (±₦20k)")
        } else if (budgetDiff <= 40_000) {
            score += 12
            highlights.add("Close Budget Range")
        }

        // 4. Department & Faculty proximity (up to 15 pts)
        if (studentA.department == studentB.department) {
            score += 15
            highlights.add("Same Dept: ${studentA.department.take(20)}...")
        } else if (studentA.faculty == studentB.faculty) {
            score += 10
            highlights.add("Same Faculty")
        }

        // 5. Academic level proximity
        if (studentA.level == studentB.level) {
            score += 8
            highlights.add("Same Level: ${studentA.level.shortLabel}")
        }

        // 6. Shared habits
        val sharedHabits = studentA.habits.intersect(studentB.habits.toSet())
        if (sharedHabits.isNotEmpty()) {
            score += min(15, sharedHabits.size * 4)
            val sampleHabit = when (sharedHabits.first()) {
                LifestyleHabit.NIGHT_OWL -> "Both Night Owls"
                LifestyleHabit.EARLY_BIRD -> "Both Early Birds"
                LifestyleHabit.STUDY_IN_SILENCE -> "Quiet Study Style"
                LifestyleHabit.VERY_CLEAN_DAILY -> "Neat & Organized"
                LifestyleHabit.NON_SMOKER -> "Non-Smokers"
                else -> "Compatible Habits"
            }
            highlights.add(sampleHabit)
        }

        val clampedScore = min(98, max(52, score))
        return MatchResult(clampedScore, highlights.take(3))
    }
}
