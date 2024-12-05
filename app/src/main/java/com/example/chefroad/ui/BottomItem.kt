package com.example.chefroad.ui

import androidx.annotation.DrawableRes
import com.example.chefroad.R

sealed class BottomItem(val route: String, @DrawableRes val icon: Int) {
    object Home : BottomItem("home", R.drawable.menu_home)
    object Map : BottomItem("map", R.drawable.menu_map)
    object My : BottomItem("my", R.drawable.menu_my)
}