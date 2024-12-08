package com.example.chefroad.feature.model

data class Favorite(
    val userId: String = "",
    val restaurantId: Int,
    val restaurantName: String = "",
    val isFavorite: Boolean = false
)
