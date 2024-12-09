package com.example.chefroad.feature.restaurant.data

import android.content.Context

fun mapAllergyIcons(context: Context, allergyIcons: List<String>): List<Int> {
    return allergyIcons.mapNotNull { iconName ->
        val resourceName = "allergy_icon_$iconName"
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        if (resId != 0) resId else null
    }
}