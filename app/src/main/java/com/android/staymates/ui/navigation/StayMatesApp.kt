package com.android.staymates.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.staymates.ui.screens.CalculatorScreen
import com.android.staymates.ui.screens.ChatScreen
import com.android.staymates.ui.screens.ListingsScreen
import com.android.staymates.ui.screens.MatchesScreen
import com.android.staymates.ui.screens.ProfileScreen

@Composable
fun StayMatesApp() {
    val navController = rememberNavController()

    val bottomTabs = listOf(
        BottomTab(AppDestination.Listings, "Listings", Icons.Filled.Home),
        BottomTab(AppDestination.Matches, "Matches", Icons.Filled.Star),
        BottomTab(AppDestination.Chat, "Chat", Icons.Filled.Chat),
        BottomTab(AppDestination.Calculator, "Split", Icons.Filled.Calculate),
        BottomTab(AppDestination.Profile, "Profile", Icons.Filled.Person),
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                bottomTabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.destination.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.label)
                        },
                        label = {
                            Text(tab.label)
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Listings.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestination.Listings.route) { ListingsScreen() }
            composable(AppDestination.Matches.route) { MatchesScreen() }
            composable(AppDestination.Chat.route) { ChatScreen() }
            composable(AppDestination.Calculator.route) { CalculatorScreen() }
            composable(AppDestination.Profile.route) { ProfileScreen() }
        }
    }
}

private data class BottomTab(
    val destination: AppDestination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)
