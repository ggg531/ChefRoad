package com.example.chefroad.feature.restaurant

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class FilterRest(private val context: Context) {

    data class Restaurant(
        val id: Int,
        val food_type: String,
        val restaurant_type: String,
        val menuItems: List<MenuItem>,
        val allergies: List<String>
    )

    data class MenuItem(
        val name: String,
        val price: Int
    )

    fun getFilteredRestaurants(
        foodType: String?,
        resType: String?,
        priceRange: Int?,
        allergies: List<String>?
    ): List<Int> {
        val jsonString = context.assets.open("restaurants.json").bufferedReader().use { it.readText() }
        val restaurantList: List<Restaurant> = Gson().fromJson(jsonString, object : TypeToken<List<Restaurant>>() {}.type)

        return restaurantList.filter { restaurant ->
            (foodType == null || restaurant.food_type == foodType) &&
                    (resType == null || restaurant.restaurant_type == resType) &&
                    (priceRange == null || restaurant.menuItems.any { it.price <= priceRange }) &&
                    (allergies == null || restaurant.allergies.intersect(allergies).isNotEmpty())
        }.map { it.id }
    }
}