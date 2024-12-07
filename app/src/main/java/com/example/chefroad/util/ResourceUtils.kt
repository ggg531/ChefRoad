package com.example.chefroad.util

import android.content.Context

fun getDrawableResId(context: Context, resourceName: String?): Int? {
    return resourceName?.let {
        val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
        if (resId != 0) resId else null
    }
}
