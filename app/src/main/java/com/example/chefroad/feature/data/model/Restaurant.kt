package com.example.chefroad.feature.restaurant.data.model

data class Restaurant(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val openingHours: String,
    val weeklyHours: List<String>,
    val menuItems: List<MenuItem>,
    val reviews: List<Review>,
    val imageResId: String?,
    val allergyIcons: List<Int>,
    val waitTime: String,
    val tvShow: String
)
