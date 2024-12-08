package com.example.chefroad.util

import android.content.Context

fun getImageResourceId(context: Context, imageResName: String?): Int? {
    return if (imageResName != null) {
        context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    } else {
        null
    }
}
