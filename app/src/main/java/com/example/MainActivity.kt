package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Student
import com.example.ui.AppTab
import com.example.ui.FutaViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FutaRoommateApp()
            }
        }
    }
}

@Composable
fun FutaRoommateApp(viewModel: FutaViewModel = viewModel()) {
    val currentTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
    val unreadNotifications by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()
    val matches by viewModel.roommateMatches.collectAsStateWithLifecycle()
    val listings by viewModel.filteredListings.collectAsStateWithLifecycle()
    val incomingRequests by viewModel.incomingRequests.collectAsStateWithLifecycle()
    val sentRequests by viewModel.sentRequests.collectAsStateWithLifecycle()
    val currentRoommates by viewModel.currentRoommates.collectAsStateWithLifecycle()
    val formerRoommates by viewModel.formerRoommates.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val adminStats by viewModel.adminStats.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val listingLocationFilter by viewModel.listingLocationFilter.collectAsStateWithLifecycle()

    // Sub-screens
    val activeChatRecipientId by viewModel.activeChatRecipientId.collectAsStateWithLifecycle()
    val inspectingStudentId by viewModel.inspectingStudentId.collectAsStateWithLifecycle()
    val showAdminDashboard by viewModel.showAdminDashboard.collectAsStateWithLifecycle()
    val showRoommateHistory by viewModel.showRoommateHistory.collectAsStateWithLifecycle()
    val showCreateListing by viewModel.showCreateListing.collectAsStateWithLifecycle()
    val showEditAccommodation by viewModel.showEditAccommodation.collectAsStateWithLifecycle()
    val showNotifications by viewModel.showNotifications.collectAsStateWithLifecycle()
    val showUserSwitcher by viewModel.showUserSwitcher.collectAsStateWithLifecycle()
    val showFilterSheet by viewModel.showFilterSheet.collectAsStateWithLifecycle()

    val pendingRequestsCount = incomingRequests.count { it.status == com.example.data.model.RequestStatus.PENDING }

    // Chat screen when chatting
    if (activeChatRecipientId != null) {
        val chatRecipient = allStudents.find { it.id == activeChatRecipientId } ?: currentUser
        val chatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()

        ChatScreen(
            currentUserId = currentUser.id,
            recipient = chatRecipient,
            messages = chatMessages,
            onSendMessage = { text, attachment ->
                viewModel.sendMessage(text, attachment)
            },
            onEstablishRoommate = {
                viewModel.establishRoommate(chatRecipient.id, chatRecipient.preferredLocation)
            },
            onBack = { viewModel.closeChat() }
        )
        return
    }

    // Admin Dashboard screen
    if (showAdminDashboard) {
        AdminDashboardScreen(
            stats = adminStats,
            students = allStudents,
            reports = reports,
            onVerifyStudent = { studentId ->
                viewModel.verifyStudent(studentId)
            },
            onBack = { viewModel.setShowAdminDashboard(false) }
        )
        return
    }

    // Roommate History screen (Feature 9)
    if (showRoommateHistory) {
        RoommateHistoryScreen(
            currentRoommates = currentRoommates,
            formerRoommates = formerRoommates,
            onTransitionStatus = { relId, status ->
                viewModel.transitionRoommateStatus(relId, status)
            },
            onMessageRoommate = { otherId ->
                viewModel.setShowRoommateHistory(false)
                viewModel.openChat(otherId)
            },
            onBack = { viewModel.setShowRoommateHistory(false) }
        )
        return
    }

    // Main Tab Scaffold
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FutaTopBar(
                currentUser = currentUser,
                unreadNotifications = unreadNotifications,
                onUserSwitcherClick = { viewModel.setShowUserSwitcher(true) },
                onNotificationsClick = { viewModel.setShowNotifications(true) },
                onAdminClick = { viewModel.setShowAdminDashboard(true) }
            )
        },
        bottomBar = {
            FutaBottomBar(
                currentTab = currentTab,
                pendingRequestsCount = pendingRequestsCount,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        currentUser = currentUser,
                        topMatches = matches,
                        activeListingsCount = listings.size,
                        currentRoommatesCount = currentRoommates.size,
                        onNavigateTab = { tab -> viewModel.selectTab(tab) },
                        onViewProfile = { id -> viewModel.inspectStudent(id) },
                        onRequestConnect = { id -> viewModel.inspectStudent(id) },
                        onEditAccommodation = { viewModel.setShowEditAccommodation(true) },
                        onLocationSelected = { loc ->
                            viewModel.updateFilters(filters.copy(location = loc))
                        }
                    )
                }

                AppTab.DISCOVER -> {
                    DiscoverScreen(
                        matches = matches,
                        filters = filters,
                        onSearchQueryChange = { q ->
                            viewModel.updateFilters(filters.copy(query = q))
                        },
                        onFilterChange = { f -> viewModel.updateFilters(f) },
                        onResetFilters = { viewModel.resetFilters() },
                        onOpenFilterSheet = { viewModel.setShowFilterSheet(true) },
                        onViewProfile = { id -> viewModel.inspectStudent(id) },
                        onRequestConnect = { id -> viewModel.inspectStudent(id) }
                    )
                }

                AppTab.LISTINGS -> {
                    ListingsScreen(
                        listings = listings,
                        currentUserId = currentUser.id,
                        selectedLocation = listingLocationFilter,
                        onLocationFilterChange = { loc -> viewModel.setListingLocationFilter(loc) },
                        onContactOwner = { ownerId -> viewModel.openChat(ownerId) },
                        onToggleOccupied = { listId -> viewModel.toggleListingOccupied(listId) },
                        onCreateListingClick = { viewModel.setShowCreateListing(true) }
                    )
                }

                AppTab.REQUESTS -> {
                    val messagesMap = mapOf(
                        "student_kazeem" to (viewModel.activeChatMessages.value.ifEmpty {
                            listOf(
                                com.example.data.model.ChatMessage(
                                    conversationId = "student_kazeem",
                                    senderId = "student_kazeem",
                                    text = "Hey! Just reminding you about the prepaid meter unit recharge.",
                                    timestampFormatted = "10:15 AM"
                                )
                            )
                        }),
                        "student_chioma" to listOf(
                            com.example.data.model.ChatMessage(
                                conversationId = "student_chioma",
                                senderId = "student_chioma",
                                text = "Yes Tunde! You can inspect the flat anytime after lectures.",
                                timestampFormatted = "Yesterday"
                            )
                        )
                    )

                    RequestsScreen(
                        incomingRequests = incomingRequests,
                        sentRequests = sentRequests,
                        allStudents = allStudents,
                        messagesMap = messagesMap,
                        onAcceptRequest = { reqId -> viewModel.acceptRequest(reqId) },
                        onRejectRequest = { reqId -> viewModel.rejectRequest(reqId) },
                        onCancelRequest = { reqId -> viewModel.cancelRequest(reqId) },
                        onOpenChat = { otherId -> viewModel.openChat(otherId) }
                    )
                }

                AppTab.PROFILE -> {
                    ProfileScreen(
                        student = currentUser,
                        currentRoommatesCount = currentRoommates.size,
                        formerRoommatesCount = formerRoommates.size,
                        onEditAccommodation = { viewModel.setShowEditAccommodation(true) },
                        onOpenRoommateHistory = { viewModel.setShowRoommateHistory(true) },
                        onOpenAdminDashboard = { viewModel.setShowAdminDashboard(true) },
                        onOpenUserSwitcher = { viewModel.setShowUserSwitcher(true) },
                        onTogglePhonePrivacy = { viewModel.togglePhonePrivacy() },
                        onToggleReligionPrivacy = { viewModel.toggleReligionPrivacy() }
                    )
                }
            }

            // Dialogs & Modals
            if (inspectingStudentId != null) {
                val student = allStudents.find { it.id == inspectingStudentId }
                if (student != null) {
                    val matchResult = viewModel.roommateMatches.value.find { it.first.id == student.id }?.second
                        ?: com.example.data.repository.MatchResult(85, listOf("Area Match", "Study Habits"))

                    StudentDetailModal(
                        student = student,
                        matchResult = matchResult,
                        onDismiss = { viewModel.closeInspectingStudent() },
                        onSendRequest = { note ->
                            viewModel.sendRoommateRequest(student.id, note)
                        }
                    )
                }
            }

            if (showCreateListing) {
                CreateListingModal(
                    onDismiss = { viewModel.setShowCreateListing(false) },
                    onSubmit = { title, loc, total, ind, spaces, rooms, type, facs, desc, date ->
                        viewModel.createListing(title, loc, total, ind, spaces, rooms, type, facs, desc, date)
                    }
                )
            }

            if (showEditAccommodation) {
                EditAccommodationModal(
                    student = currentUser,
                    onDismiss = { viewModel.setShowEditAccommodation(false) },
                    onSave = { status, loc, budget, apt ->
                        viewModel.updateAccommodation(status, loc, budget, apt)
                    }
                )
            }

            if (showNotifications) {
                NotificationsDialog(
                    notifications = notifications,
                    onDismiss = { viewModel.setShowNotifications(false) }
                )
            }

            if (showUserSwitcher) {
                UserSwitcherDialog(
                    students = allStudents,
                    currentUserId = currentUser.id,
                    onSelectUser = { uid -> viewModel.switchUser(uid) },
                    onDismiss = { viewModel.setShowUserSwitcher(false) }
                )
            }

            if (showFilterSheet) {
                FilterSheet(
                    filters = filters,
                    onApply = { f -> viewModel.updateFilters(f) },
                    onReset = { viewModel.resetFilters() },
                    onDismiss = { viewModel.setShowFilterSheet(false) }
                )
            }
        }
    }
}
