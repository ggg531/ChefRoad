package com.example.chefroad

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.feature.mypage.MyFavoriteScreen
import com.example.chatapp.feature.mypage.MyReviewScreen
import com.example.chatapp.feature.mypage.mypage_main
import com.example.chatapp.feature.restaurant.RestaurantDetailScreen
import com.example.chatapp.feature.restaurant.restaurant_list
import com.example.chefroad.feature.auth.signin.SignInScreen
import com.example.chefroad.feature.auth.signup.SignUpScreen
import com.example.chefroad.feature.home.HomeScreen
import com.example.chefroad.feature.map.MapScreen
import com.example.chefroad.feature.my.MyScreen
import com.example.chefroad.feature.restaurant.FilterScreen
import com.example.chefroad.feature.restaurant.RestaurantScreen
import com.example.chefroad.feature.review.ReviewScreen
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
                if (currentRoute in listOf("home", "map", "restaurants/{tvShow}", "mypage")) {
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
                //추가
                composable("restaurant_detail/{restaurantName}") { backStackEntry ->
                    val restaurantName = backStackEntry.arguments?.getString("restaurantName") ?: ""
                    RestaurantDetailScreen(navController = navController, restaurantName = restaurantName)
                }
                composable("restaurants/{tvShow}") { backStackEntry ->
                    val tvShow = backStackEntry.arguments?.getString("tvShow") ?: "BLACKWHITE"
                    restaurant_list(navController = navController, selectedTvShow = tvShow)
                }

                composable("myreview/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")

                    if (id == null) {
                        Text("Error: User ID not found", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
                    } else {
                        MyReviewScreen(id = id, navController = navController)
                    }
                }
                composable("myfavorite/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")

                    if (id == null) {
                        Text("Error: User ID not found", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
                    } else {
                        MyFavoriteScreen(id = id, navController = navController)
                    }
                }
                composable(route = "mypage") {
                    mypage_main(navController = navController)
                }
                composable(route = "myreview") {
                    MyReviewScreen(navController = navController)
                }
                composable(route = "myfavorite") {
                    MyFavoriteScreen(navController = navController)
                }

                composable(route = "review/{restaurantName}") { backStackEntry ->
                    val restaurantName = backStackEntry.arguments?.getString("restaurantName")
                    restaurantName?.let {
                        ReviewScreen(navController) // restaurantName을 ReviewScreen에 전달
                    }
                }

            }
        }
    }
}
