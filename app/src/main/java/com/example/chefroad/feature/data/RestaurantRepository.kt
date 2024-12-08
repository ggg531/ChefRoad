package com.example.chefroad.feature.restaurant.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.chefroad.R
import com.example.chefroad.feature.data.model.LocationMap
import com.example.chefroad.feature.data.model.ResFilter
import com.example.chefroad.feature.data.model.TvShow
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

fun loadLocationMaps(context: Context): List<LocationMap> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.restaurants)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<List<LocationMap>>() {}.type
    val locations = Gson().fromJson<List<LocationMap>>(reader, type)

    return locations.map { location ->
        location.copy(
            category = determineTvShowCategory(location.id)
        )
    }
}

private fun determineTvShowCategory(id: Int): TvShow {
    return when (id) {
        in 1..20 -> TvShow.BLACKWHITE
        in 21..40 -> TvShow.WEDNESDAY
        in 41..60 -> TvShow.LINEUP
        else -> throw IllegalArgumentException("Unknown TvShow category for ID: $id")
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