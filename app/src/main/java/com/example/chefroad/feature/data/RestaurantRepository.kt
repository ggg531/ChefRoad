package com.example.chefroad.feature.restaurant.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.data.model.Restaurant // 올바른 경로로 수정
import java.io.InputStream
import java.io.InputStreamReader

fun mapAllergyIcons(context: Context, allergyIcons: List<String>): List<Int> {
    return allergyIcons.mapNotNull { iconName ->
        val resourceName = "allergy_icon_$iconName"
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        if (resId != 0) resId else null
    }
}

fun loadRestaurants(context: Context): List<Restaurant> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.restaurants)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<Restaurant>>() {}.type
    val restaurants = Gson().fromJson<List<Restaurant>>(reader, type)

    // Allergy Icons 매핑
    return restaurants.map { restaurant ->
        restaurant.copy(
            allergyIcons = mapAllergyIcons(context, restaurant.allergyIcons as List<String>)
        )
    }
}

