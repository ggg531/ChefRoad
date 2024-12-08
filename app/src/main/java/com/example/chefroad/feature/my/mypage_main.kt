package com.example.chatapp.feature.mypage

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chefroad.R

import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.chatapp.feature.restaurant.RestaurantFavoriteViewModelFactory
import com.example.chefroad.feature.restaurant.RestaurantViewModel
import com.example.chefroad.feature.review.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mypage_main(
    navController: NavHostController,
    reviewViewModel: ReviewViewModel = hiltViewModel() // hiltViewModel 대신 viewModel로 변경
) {
    //val viewModel: RestaurantFavoriteViewModel = viewModel()
    //val restaurantFavoriteViewModel: RestaurantFavoriteViewModel = viewModel() // viewModel로 변경

    val restaurantViewModel: RestaurantViewModel = hiltViewModel() // viewModel로 변경

    val favoriteRestaurants by restaurantViewModel.favoriteRestaurants.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val mypagename = user?.email
    val currentUserId = user?.uid

    // 사용자가 작성한 리뷰만 필터링
    val userReviews = reviews.filter { it.id == currentUserId }


    LaunchedEffect(Unit) {
        reviewViewModel.getReview() // 리뷰 목록 업데이트
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "마이페이지",
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { Firebase.auth.signOut() }) { // 로그아웃 버튼
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }

        Row {
            Image(painter = painterResource(id = R.drawable.user_mypage), contentDescription = "user_main")
            Text(
                text = "$mypagename 님 \n 안녕하세요.",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ElevatedButton(
                onClick = { navController.navigate("myfavorite/$currentUserId")},
                modifier = Modifier.fillMaxWidth(0.4f).height(50.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
            ) {
                Text("즐겨찾기")
            }
            ElevatedButton(
                onClick = { navController.navigate("myreview/$currentUserId") }, //  navController.navigate("myreview")
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {
                Text("리뷰")
            }


        }

        /*
        // 식당 목록으로 이동 버튼
        BottomAppBar {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                // 식당페이지 이동 버튼
                IconButton(
                    onClick = { navController.navigate("HOME") },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(painter = painterResource(id = R.drawable.restaurant), contentDescription = "restbutton")
                }
                // 마이페이지 이동 버튼
                IconButton(
                    onClick = { navController.navigate("mypage") },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(painter = painterResource(id = R.drawable.user_bottom), contentDescription = "mypagebutton")
                }
            }
        }*/
    }
}


