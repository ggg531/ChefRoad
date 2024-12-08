package com.example.chatapp.feature.mypage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.chatapp.feature.restaurant.RestaurantItem
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.RestaurantViewModel
import com.google.firebase.auth.FirebaseAuth



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyFavoriteScreen (
    id: String? = null,
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel = hiltViewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val currentUserId = user?.uid // 현재 사용자 ID

    val favoriteRestaurants by restaurantViewModel.favoriteRestaurants.collectAsState(emptyList())//initial = emptyList()

    Log.d("FavoriteToggle", "Favorite restaurants in UI: $favoriteRestaurants") // 추가된 로그

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "나의 즐겨찾기",
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (favoriteRestaurants.isEmpty()) {
                    emptyFavorite(navController)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(favoriteRestaurants) { restaurant ->
                            RestaurantItem(
                                restaurant = restaurant,
                                viewModel = restaurantViewModel,
                                onClick = { /* Navigate to restaurant details */ }
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun emptyFavorite(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Column (
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.emptyfavorite),
                contentDescription = "작성리뷰x",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("즐겨찾기한 식당이 비어있어요.", modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("HOME") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.redpink),
                    contentColor = Color.White
                )
            ){
                Text(text = "식당 보러가기")
            }
        }
    }

}



