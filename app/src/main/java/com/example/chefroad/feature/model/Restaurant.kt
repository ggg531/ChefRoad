package com.example.chefroad.feature.model

data class Restaurant(
    val id: Int,
    val name: String,
    val phoneNumber: String?,
    val address: String?,
    val openingHours: String?,
    val weeklyHours: List<String>,
    val menuItems: List<MenuItem>,
    var reviews: List<Review>,
    val imageResId: String?,
    val allergyIcons: List<Int>,
    val waitTime: String?,
    val tvShow: String?,
    val distance: Double,
    val food_type: String?,
    val restaurant_type: String?,
    val allergies: List<String>,
    var isFavorite: Boolean ?= false
)

data class MenuItem(
    val name: String,
    val price: Int,
    val imageResId: String?
)

data class RestaurantResponse(
    val restaurants: List<Restaurant>
)
