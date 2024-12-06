package com.example.chefroad

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chefroad.feature.auth.signin.SignInScreen
import com.example.chefroad.feature.auth.signup.SignUpScreen
import com.example.chefroad.feature.home.HomeScreen
import com.example.chefroad.feature.map.MapScreen
import com.example.chefroad.feature.my.MyScreen
import com.example.chefroad.feature.restaurant.FilterScreen
import com.example.chefroad.feature.restaurant.RestaurantScreen
import com.example.chefroad.ui.BottomBar
import com.example.chefroad.ui.SplashScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                if (currentRoute in listOf("home", "map", "filter", "my")) {
                    BottomBar(navController = navController)
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {
                composable("splash") {
                    SplashScreen(navController = navController)
                }
                composable("signin") {
                    SignInScreen(navController = navController)
                }
                composable(route = "signup") {
                    SignUpScreen(navController = navController)
                }
                composable(route = "home") {
                    HomeScreen(navController = navController)
                }
                composable("restaurant") {
                    RestaurantScreen(navController = navController)
                }
                composable("filter") {
                    FilterScreen(navController = navController)
                }
                composable(route = "map") {
                    MapScreen()
                }
                composable(route = "my") {
                    MyScreen(navController = navController)
                }
            }
        }
    }
}
