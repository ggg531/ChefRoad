package com.example.chefroad.feature.model

data class Review(
    val id: String = "",
    val restaurantName: String = "",
    val content: String,
    val createAt: Long = System.currentTimeMillis()
)