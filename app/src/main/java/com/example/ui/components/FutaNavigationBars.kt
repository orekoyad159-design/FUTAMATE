package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.model.Student
import com.example.ui.AppTab
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FutaTopBar(
    currentUser: Student,
    unreadNotifications: Int,
    onUserSwitcherClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.testTag("futa_top_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // FUTA Brand Emblem
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(FutaNavyDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "FUTA Emblem",
                        tint = FutaGold,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FUTA Roomies",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = FutaNavyDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = FutaGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Akure",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutaGoldDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    // Active student quick badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onUserSwitcherClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "As: ${currentUser.fullName.split(" ").first()} (${currentUser.level.shortLabel})",
                            fontSize = 11.sp,
                            color = Neutral500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch profile",
                            tint = Neutral500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        actions = {
            // Admin button
            IconButton(
                onClick = onAdminClick,
                modifier = Modifier
                    .testTag("admin_dashboard_button")
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AdminPanelSettings,
                    contentDescription = "Admin Dashboard",
                    tint = FutaNavyMedium
                )
            }

            // Notifications with badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .testTag("notification_button")
                    .size(44.dp)
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White
                            ) {
                                Text("$unreadNotifications")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // User Avatar trigger
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(currentUser.avatarColorHex))
                    .clickable { onUserSwitcherClick() }
                    .testTag("user_switcher_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentUser.avatarInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    )
}

@Composable
fun FutaBottomBar(
    currentTab: AppTab,
    pendingRequestsCount: Int,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("futa_bottom_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        AppTab.values().forEach { tab ->
            val isSelected = currentTab == tab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    if (tab == AppTab.REQUESTS && pendingRequestsCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = FutaGoldDark,
                                    contentColor = Color.White
                                ) {
                                    Text("$pendingRequestsCount")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = getTabIcon(tab, isSelected),
                                contentDescription = tab.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = getTabIcon(tab, isSelected),
                            contentDescription = tab.label
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FutaNavyDark,
                    selectedTextColor = FutaNavyDark,
                    indicatorColor = FutaGold.copy(alpha = 0.25f),
                    unselectedIconColor = Neutral500,
                    unselectedTextColor = Neutral500
                ),
                modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
            )
        }
    }
}

private fun getTabIcon(tab: AppTab, isSelected: Boolean) = when (tab) {
    AppTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
    AppTab.DISCOVER -> if (isSelected) Icons.Filled.Group else Icons.Outlined.Group
    AppTab.LISTINGS -> if (isSelected) Icons.Filled.Apartment else Icons.Outlined.Apartment
    AppTab.REQUESTS -> if (isSelected) Icons.Filled.Mail else Icons.Outlined.MailOutline
    AppTab.PROFILE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.PersonOutline
}
