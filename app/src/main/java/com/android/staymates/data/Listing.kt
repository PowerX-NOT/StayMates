package com.android.staymates.data

data class Listing(
    val id: String,
    val title: String,
    val location: String,
    val monthlyRent: Int,
    val deposit: Int,
    val rooms: Int,
    val description: String,
)
