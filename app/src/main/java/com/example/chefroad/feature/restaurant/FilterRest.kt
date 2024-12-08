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
}