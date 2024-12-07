package com.example.chefroad.feature.data.model

import com.example.chefroad.feature.restaurant.data.model.MenuItem

data class ResFilter(
    val name: String,
    val foodType: String,
    val restaurantType: String,
    val menuItems: List<MenuItem>,
    val allergies: List<String>
)