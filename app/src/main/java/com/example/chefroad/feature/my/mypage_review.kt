package com.example.chatapp.feature.mypage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.RestaurantViewModel
import com.example.chefroad.feature.review.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyReviewScreen (
    id: String? = null,
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel = hiltViewModel(),
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val currentUserId = user?.uid // 현재 사용자 ID

    //val restaurants by restaurantViewModel.restaurants.collectAsState(initial = emptyList())
    //val restaurants = loadRestaurants()
    val reviews by reviewViewModel.reviews.collectAsState()
    val userReviews = reviews.filter { it.id == currentUserId }
    val restaurants by restaurantViewModel.restaurants.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Column {
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
                        text = "나의 리뷰",
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif, // SansSerif로 설정
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

        },
        content = { paddingValues ->
            Column (
                modifier = Modifier.padding(paddingValues)
            ) {
                if (userReviews.isEmpty()) {
                    emptyReview(navController)
                } else {
                    userReviews.forEach { review ->
                        val restaurant = restaurants.find { it.name == review.restaurantName }
                        val restaurantName = restaurant?.name ?: "Unknown"

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Gray.copy(alpha = 0.1f)) // 회색 배경 추가
                                .padding(8.dp) // 내용과 배경 사이에 여백 추가
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) // 둥근 모서리 추가
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),

                                ){

                                Image(
                                    painter = painterResource(id = R.drawable.star_on),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(end = 8.dp)
                                )

                                var expanded by remember { mutableStateOf(false) }
                                var selectedSortOption by remember { mutableStateOf("리뷰 수정하기") }

                                Column {
                                    Row {
                                        Text(
                                            text = "${review.restaurantName}",
                                            modifier = Modifier.padding(8.dp).clickable {
                                                navController.navigate("restaurant_detail/${review.restaurantName}")
                                            },
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        IconButton(
                                            onClick = { expanded = !expanded },

                                            ) {
                                            Icon(painter = painterResource(id = R.drawable.review_setting), contentDescription = "리뷰 수정/삭제")
                                        }
                                    }
                                    HorizontalDivider(
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        text = "${review.content}",
                                        modifier = Modifier.padding(8.dp).clickable {
                                            navController.navigate("restaurant_detail/${review.restaurantName}")
                                        }
                                    )
                                }


                                // 드롭다운 메뉴
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },

                                    ) {
                                    val options = listOf("리뷰 수정하기", "리뷰 삭제하기")
                                    options.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                expanded = false
                                                when(option) {
                                                    "리뷰 수정하기" -> {
                                                        navController.navigate("edit_review/${review.id}/${review.content}") // 수정 화면으로 이동
                                                    }
                                                    "리뷰 삭제하기" -> {
                                                        reviewViewModel.deleteReview(review.id) // 리뷰 삭제
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun emptyReview(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Column (
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.emptyreview),
                contentDescription = "작성리뷰x",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("아직 리뷰를 작성하지 않았어요.", modifier = Modifier.padding(8.dp))
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

@Composable
fun EditReviewScreen(reviewId: String, currentContent: String, reviewViewModel: ReviewViewModel) {
    var content by remember { mutableStateOf(currentContent) }

    Column {
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("리뷰 내용") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            reviewViewModel.modifyReview(reviewId, content)  // 수정된 리뷰 Firebase에 업데이트
        }) {
            Text("수정 완료")
        }
    }
}










