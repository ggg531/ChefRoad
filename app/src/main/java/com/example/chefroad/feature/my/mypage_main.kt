package com.example.chatapp.feature.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.RestaurantViewModel
import com.example.chefroad.feature.review.ReviewViewModel
import com.example.chefroad.ui.theme.DarkGray
import com.example.chefroad.ui.theme.Purple2
import com.example.chefroad.ui.theme.Purple3
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun mypage_main(
    navController: NavHostController,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    val restaurantViewModel: RestaurantViewModel = hiltViewModel()

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

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "마이 페이지",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                    IconButton(
                        onClick = {
                            Firebase.auth.signOut()
                            navController.navigate("signin" )
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "logout",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Divider(color = DarkGray, thickness = 1.dp)
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Row {
                    Image(painter = painterResource(id = R.drawable.user_mypage), contentDescription = "user_main")
                    Text(
                        text = "$mypagename 님 \n안녕하세요.",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Black
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Divider(color = DarkGray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navController.navigate("myfavorite/$currentUserId") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple3,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("즐겨찾기")
                        }
                        Button(
                            onClick = { navController.navigate("myreview/$currentUserId") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple3,
                                contentColor = Color.White,
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("내가 쓴 리뷰")
                        }
                    }
                }
            }
        }
    )
}


