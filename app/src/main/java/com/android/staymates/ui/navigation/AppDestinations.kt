package com.android.staymates.ui.navigation

sealed class AppDestination(val route: String) {
    data object Listings : AppDestination("listings")
    data object ListingDetails : AppDestination("listingDetails/{listingId}") {
        fun createRoute(listingId: String): String = "listingDetails/$listingId"
    }
    data object CreateListing : AppDestination("createListing")
    data object Matches : AppDestination("matches")
    data object Chat : AppDestination("chat")
    data object Calculator : AppDestination("calculator")
    data object Profile : AppDestination("profile")
}
