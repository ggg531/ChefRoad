package com.example.chefroad.feature.utils

import android.content.Context
import com.example.chefroad.R
import com.example.chefroad.feature.model.Restaurant
import com.example.chefroad.feature.model.RestaurantResponse
import com.google.gson.Gson


fun loadRestaurantsFromJson(context: Context): List<Restaurant> {
    val inputStream = context.resources.openRawResource(R.raw.restaurants)  // raw 폴더 내의 restaurants.json 파일
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val restaurantResponse = Gson().fromJson(jsonString, RestaurantResponse::class.java)
    return restaurantResponse.restaurants
}


