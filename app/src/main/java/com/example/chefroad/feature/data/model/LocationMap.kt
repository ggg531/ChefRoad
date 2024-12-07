package com.example.chefroad.feature.data.model

import com.example.chefroad.feature.restaurant.data.model.Restaurant

data class LocationMap(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: Category
)

enum class Category {
    BLACKWHITE,
    WEDNESDAY,
    LINEUP
}


fun List<Restaurant>.toLocationMapList(): List<LocationMap> {
    return this.map { restaurant ->
        LocationMap(
            id = restaurant.id,
            name = restaurant.name,
            latitude = restaurant.latitude,
            longitude = restaurant.longitude,
            category = determineCategory(restaurant.id)
        )
    }
}

private fun determineCategory(id: Int): Category {
    return when (id) {
        in 1..20 -> Category.BLACKWHITE
        in 21..40 -> Category.WEDNESDAY
        in 41..60 -> Category.LINEUP
        else -> throw IllegalArgumentException("Unknown category for ID: $id")
    }
}