package com.example.chatapp.feature.restaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.data.model.MenuItem
import com.example.chefroad.feature.restaurant.data.model.Review
import com.example.chefroad.feature.restaurantinfo.MenuTabContent
import com.example.chefroad.feature.restaurantinfo.ReviewTabContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantInfo2(
    navController: NavHostController,
    restaurantName: String = "진진",
    phoneNumber: String = "0507-1447-8879",
    address: String = "서울시 마포구 월드컵북로1길 60",
    openingHours: String = "정기휴무",
    weeklyHours: List<String> = listOf(
        "월요일: 정기휴무", "화요일: 12:00 ~ 22:00", "수요일: 12:00 ~ 22:00",
        "목요일: 12:00 ~ 22:00", "금요일: 12:00 ~ 23:00", "토요일: 12:00 ~ 23:00", "일요일: 정기휴무"
    ),
    menuItems: List<MenuItem> = listOf(
        MenuItem("멘보샤(회원가)", 22000, "menu_13_01"),
        MenuItem("대게살볶음(회원가)", 29600, "menu_13_02"),
        MenuItem("깐풍기(회원가)", 22400, "menu_13_03"),
        MenuItem("마파두부(회원가)", 17600, "menu_13_04"),
        MenuItem("칭찡우럭(회원가)", 34400, "menu_13_05"),
        MenuItem("어향가지(회원가)", 20000, "menu_13_06"),
        MenuItem("카이란소고기(회원가)", 31600, "menu_13_07"),
        MenuItem("전복팔보채(회원가)", 43200, "menu_13_08"),
        MenuItem("간쇼새우(회원가)", 24000, "menu_13_09"),
        MenuItem("소고기 양상추 쌈(회원가)", 30000, "menu_13_10"),
        MenuItem("홍믄생선(회원가)", 45000, "menu_13_11")
    ),
    reviews: List<Review> = listOf(
        Review("Alice", "맛있어요!", "person"),
        Review("Bob", "서비스가 좋아요!", "person")
    ),
    imageResId: Int = R.drawable.restaurant_13_main,
    allergyIcons: List<Int> = listOf(R.drawable.allergy_icon_egg, R.drawable.allergy_icon_fish),
    waitTime: String = "30분 예상"
) {
    var selectedTab by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("식당 상세 정보") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate("review/$restaurantName") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "리뷰로 이동",
                        tint = Color.Black
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Restaurant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        allergyIcons.forEach { iconResId ->
                            Image(
                                painter = painterResource(id = iconResId),
                                contentDescription = "Allergy Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "예상 대기시간: $waitTime",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = restaurantName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                    Text(
                        text = "전화번호: $phoneNumber",
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    Text(
                        text = "주소: $address",
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "영업시간: $openingHours",
                            fontSize = 14.sp,
                            color = androidx.compose.ui.graphics.Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(
                                id = if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { expanded = !expanded }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                weeklyHours.forEach { hours ->
                    Text(text = hours, fontSize = 14.sp, color = androidx.compose.ui.graphics.Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("메뉴") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("리뷰") }
            )
        }

        when (selectedTab) {
            0 -> MenuTabContent(menuItems = menuItems)
            1 -> ReviewTabContent(reviews = reviews)
        }
    }
}


