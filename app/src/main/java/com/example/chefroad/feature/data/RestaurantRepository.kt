package com.example.chefroad.feature.restaurant.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.chefroad.R
import com.example.chefroad.feature.data.model.ResFilter
import com.example.chefroad.feature.data.model.TvShow
import com.example.chefroad.feature.restaurant.data.model.Restaurant 
import java.io.InputStream
import java.io.InputStreamReader

fun mapAllergyIcons(context: Context, allergyIcons: List<String>): List<Int> {
    return allergyIcons.mapNotNull { iconName ->
        try {
            val resourceName = "allergy_icon_$iconName"
            val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
            if (resId != 0) resId else {
                Log.e("mapAllergyIcons", "Resource not found for allergy icon: $iconName")
                null
            }
        } catch (e: Exception) {
            Log.e("mapAllergyIcons", "Error mapping allergy icon: $iconName", e)
            null
        }
    }
}



fun loadRestaurants(context: Context): List<Restaurant> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.restaurants)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<Restaurant>>() {}.type

    val gson = Gson()
    val rawRestaurants = gson.fromJson<List<Restaurant>>(reader, type)

    return rawRestaurants.map { rawRestaurant ->
        rawRestaurant.copy(
            allergyIcons = mapAllergyIcons(context, rawRestaurant.allergyIcons as List<String>)
        )
    }
}


fun loadResFilters(context: Context): List<ResFilter> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.restaurants)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<ResFilter>>() {}.type
    val resFilters = Gson().fromJson<List<ResFilter>>(reader, type)

    return resFilters.map { resFilter ->
        resFilter.copy(
            //
        )
    }
}