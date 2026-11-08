package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.FutaRepository
import com.example.data.repository.MatchResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab(val label: String) {
    HOME("Home"),
    DISCOVER("Discover"),
    LISTINGS("Listings"),
    REQUESTS("Requests"),
    PROFILE("Profile")
}

data class DiscoverFilters(
    val query: String = "",
    val department: String? = null,
    val level: AcademicLevel? = null,
    val accommodationStatus: AccommodationStatus? = null,
    val location: String? = null,
    val maxBudget: Int = 300_000,
    val religionFilter: String? = null
)

data class AdminStats(
    val totalStudents: Int,
    val verifiedStudents: Int,
    val activeListings: Int,
    val pendingRequests: Int,
    val currentRoommates: Int,
    val activeReports: Int
)

class FutaViewModel(
    private val repository: FutaRepository = FutaRepository()
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(AppTab.HOME)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    // Sub-screens & Dialogs
    private val _activeChatRecipientId = MutableStateFlow<String?>(null)
    val activeChatRecipientId: StateFlow<String?> = _activeChatRecipientId.asStateFlow()

    private val _inspectingStudentId = MutableStateFlow<String?>(null)
    val inspectingStudentId: StateFlow<String?> = _inspectingStudentId.asStateFlow()

    private val _showCreateListing = MutableStateFlow(false)
    val showCreateListing: StateFlow<Boolean> = _showCreateListing.asStateFlow()

    private val _showAdminDashboard = MutableStateFlow(false)
    val showAdminDashboard: StateFlow<Boolean> = _showAdminDashboard.asStateFlow()

    private val _showNotifications = MutableStateFlow(false)
    val showNotifications: StateFlow<Boolean> = _showNotifications.asStateFlow()

    private val _showEditAccommodation = MutableStateFlow(false)
    val showEditAccommodation: StateFlow<Boolean> = _showEditAccommodation.asStateFlow()

    private val _showUserSwitcher = MutableStateFlow(false)
    val showUserSwitcher: StateFlow<Boolean> = _showUserSwitcher.asStateFlow()

    private val _showRoommateHistory = MutableStateFlow(false)
    val showRoommateHistory: StateFlow<Boolean> = _showRoommateHistory.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    // Filters
    private val _filters = MutableStateFlow(DiscoverFilters())
    val filters: StateFlow<DiscoverFilters> = _filters.asStateFlow()

    private val _listingLocationFilter = MutableStateFlow<String?>(null)
    val listingLocationFilter: StateFlow<String?> = _listingLocationFilter.asStateFlow()

    val allStudents: StateFlow<List<Student>> = repository.allStudents
    val currentUserId: StateFlow<String> = repository.currentUserId
    val listings: StateFlow<List<ApartmentListing>> = repository.listings
    val requests: StateFlow<List<RoommateRequest>> = repository.requests
    val relationships: StateFlow<List<RoommateRelationship>> = repository.relationships
    val notifications: StateFlow<List<AppNotification>> = repository.notifications
    val reports: StateFlow<List<AdminReport>> = repository.reports

    val currentUser: StateFlow<Student> = combine(allStudents, currentUserId) { students, id ->
        students.find { it.id == id } ?: students.first()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, repository.getCurrentUser())

    val unreadNotificationCount: StateFlow<Int> = combine(notifications, currentUserId) { notifs, uid ->
        notifs.count { it.userId == uid && !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 1)

    // Filtered Roommates with Compatibility Score
    val roommateMatches: StateFlow<List<Pair<Student, MatchResult>>> =
        combine(allStudents, currentUser, filters) { students, user, f ->
            students
                .filter { it.id != user.id }
                .filter { student ->
                    val matchesQuery = f.query.isBlank() ||
                        student.fullName.contains(f.query, ignoreCase = true) ||
                        student.department.contains(f.query, ignoreCase = true) ||
                        student.preferredLocation.contains(f.query, ignoreCase = true) ||
                        student.bio.contains(f.query, ignoreCase = true)

                    val matchesDept = f.department == null || student.department == f.department
                    val matchesLevel = f.level == null || student.level == f.level
                    val matchesStatus = f.accommodationStatus == null || student.accommodationStatus == f.accommodationStatus
                    val matchesLocation = f.location == null || student.preferredLocation.equals(f.location, ignoreCase = true)
                    val matchesBudget = student.budgetNaira <= f.maxBudget
                    val matchesReligion = f.religionFilter == null || student.religion == f.religionFilter

                    matchesQuery && matchesDept && matchesLevel && matchesStatus && matchesLocation && matchesBudget && matchesReligion
                }
                .map { student ->
                    val match = repository.calculateCompatibility(user, student)
                    Pair(student, match)
                }
                .sortedByDescending { it.second.percentage }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val filteredListings: StateFlow<List<ApartmentListing>> =
        combine(listings, listingLocationFilter) { list, loc ->
            if (loc == null || loc == "All") list
            else list.filter { it.location.contains(loc, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val incomingRequests: StateFlow<List<RoommateRequest>> =
        combine(requests, currentUserId) { reqs, uid ->
            reqs.filter { it.receiverId == uid }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val sentRequests: StateFlow<List<RoommateRequest>> =
        combine(requests, currentUserId) { reqs, uid ->
            reqs.filter { it.senderId == uid }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val currentRoommates: StateFlow<List<RoommateRelationship>> =
        combine(relationships, currentUserId) { rels, uid ->
            rels.filter { (it.studentAId == uid || it.studentBId == uid) && it.status == RelationshipStatus.CURRENT_ROOMMATE }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val formerRoommates: StateFlow<List<RoommateRelationship>> =
        combine(relationships, currentUserId) { rels, uid ->
            rels.filter { (it.studentAId == uid || it.studentBId == uid) && it.status == RelationshipStatus.FORMER_ROOMMATE }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val activeChatMessages: StateFlow<List<ChatMessage>> =
        combine(repository.messages, activeChatRecipientId) { msgMap, recipientId ->
            if (recipientId == null) emptyList()
            else msgMap[recipientId] ?: emptyList()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val adminStats: StateFlow<AdminStats> =
        combine(allStudents, listings, requests, relationships, reports) { studs, lists, reqs, rels, reps ->
            AdminStats(
                totalStudents = studs.size,
                verifiedStudents = studs.count { it.verificationStatus == VerificationStatus.VERIFIED_FUTA_STUDENT },
                activeListings = lists.count { !it.isOccupied },
                pendingRequests = reqs.count { it.status == RequestStatus.PENDING },
                currentRoommates = rels.count { it.status == RelationshipStatus.CURRENT_ROOMMATE },
                activeReports = reps.size
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            AdminStats(5, 5, 3, 2, 1, 1)
        )

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
        _activeChatRecipientId.value = null
        _inspectingStudentId.value = null
        _showAdminDashboard.value = false
        _showRoommateHistory.value = false
    }

    fun openChat(recipientId: String) {
        _activeChatRecipientId.value = recipientId
    }

    fun closeChat() {
        _activeChatRecipientId.value = null
    }

    fun inspectStudent(studentId: String) {
        _inspectingStudentId.value = studentId
    }

    fun closeInspectingStudent() {
        _inspectingStudentId.value = null
    }

    fun setShowCreateListing(show: Boolean) {
        _showCreateListing.value = show
    }

    fun setShowAdminDashboard(show: Boolean) {
        _showAdminDashboard.value = show
    }

    fun setShowNotifications(show: Boolean) {
        _showNotifications.value = show
        if (show) repository.markNotificationsRead()
    }

    fun setShowEditAccommodation(show: Boolean) {
        _showEditAccommodation.value = show
    }

    fun setShowUserSwitcher(show: Boolean) {
        _showUserSwitcher.value = show
    }

    fun setShowRoommateHistory(show: Boolean) {
        _showRoommateHistory.value = show
    }

    fun setShowFilterSheet(show: Boolean) {
        _showFilterSheet.value = show
    }

    fun updateFilters(newFilters: DiscoverFilters) {
        _filters.value = newFilters
    }

    fun resetFilters() {
        _filters.value = DiscoverFilters()
    }

    fun setListingLocationFilter(location: String?) {
        _listingLocationFilter.value = location
    }

    fun switchUser(studentId: String) {
        repository.switchActiveUser(studentId)
        _activeChatRecipientId.value = null
        _inspectingStudentId.value = null
        _showUserSwitcher.value = false
    }

    fun sendRoommateRequest(targetStudentId: String, note: String): Boolean {
        return repository.sendRoommateRequest(targetStudentId, note)
    }

    fun acceptRequest(requestId: String) {
        repository.acceptRoommateRequest(requestId)
    }

    fun rejectRequest(requestId: String) {
        repository.rejectRoommateRequest(requestId)
    }

    fun cancelRequest(requestId: String) {
        repository.cancelRoommateRequest(requestId)
    }

    fun sendMessage(text: String, attachment: String? = null) {
        val recipientId = _activeChatRecipientId.value ?: return
        repository.sendMessage(recipientId, text, attachment)
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
        repository.createListing(
            title, location, totalRent, individualRent,
            availableSpaces, totalRooms, apartmentType, facilities, description, moveInDate
        )
        _showCreateListing.value = false
    }

    fun toggleListingOccupied(listingId: String) {
        repository.toggleListingOccupied(listingId)
    }

    fun establishRoommate(targetStudentId: String, location: String) {
        repository.establishRoommateConnection(targetStudentId, location)
    }

    fun transitionRoommateStatus(relationshipId: String, newStatus: RelationshipStatus) {
        repository.transitionRoommateStatus(relationshipId, newStatus)
    }

    fun verifyStudent(studentId: String) {
        repository.verifyStudent(studentId)
    }

    fun reportEntity(name: String, reason: String) {
        repository.reportEntity(name, reason)
    }

    fun updateAccommodation(
        status: AccommodationStatus,
        preferredLocation: String,
        budget: Int,
        apartmentDetails: ApartmentDetails?
    ) {
        repository.updateAccommodationStatus(status, preferredLocation, budget, apartmentDetails)
        _showEditAccommodation.value = false
    }

    fun togglePhonePrivacy() {
        val user = currentUser.value
        repository.updateStudentProfile(user.copy(showPhonePublicly = !user.showPhonePublicly))
    }

    fun toggleReligionPrivacy() {
        val user = currentUser.value
        repository.updateStudentProfile(user.copy(showReligionPublicly = !user.showReligionPublicly))
    }
}
