package com.example.chefroad.feature.restaurant.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.data.model.Restaurant // 올바른 경로로 수정
import java.io.InputStream
import java.io.InputStreamReader

fun loadRestaurants(context: Context): List<Restaurant> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.restaurants)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<Restaurant>>() {}.type
    return Gson().fromJson(reader, type)
}
