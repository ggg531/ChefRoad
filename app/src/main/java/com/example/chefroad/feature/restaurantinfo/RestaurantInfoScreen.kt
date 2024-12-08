package com.example.chefroad.feature.restaurantinfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chefroad.R
import com.example.chefroad.feature.restaurant.data.model.MenuItem
import com.example.chefroad.feature.restaurant.data.model.Review
import com.example.chefroad.feature.restaurant.data.model.Restaurant
import androidx.compose.ui.platform.LocalContext
import com.example.chefroad.util.getDrawableResId
import com.example.chefroad.util.getImageResourceId

@Composable
fun RestaurantInfoScreen(
    restaurantName: String,
    phoneNumber: String,
    address: String,
    openingHours: String,
    weeklyHours: List<String>,
    menuItems: List<MenuItem>,
    reviews: List<Review>,
    imageResId: Int?,
    allergyIcons: List<Int>,
    waitTime: String
) {
    var selectedTab by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(56.dp))

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
                        painter = painterResource(id = imageResId ?: R.drawable.placeholder),
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
                weeklyHours.forEachIndexed { index, hours ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dayOfWeek(index),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = hours,
                            fontSize = 14.sp,
                            color = androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
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

fun dayOfWeek(index: Int): String {
    return listOf("월", "화", "수", "목", "금", "토", "일")[index]
}

@Composable
fun MenuTabContent(menuItems: List<MenuItem>) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(menuItems) { menuItem ->
            val imageResId = getDrawableResId(context, menuItem.imageResId) ?: R.drawable.placeholder

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Menu Image",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = menuItem.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${menuItem.price}원",
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
            Divider()
        }
    }
}



@Composable
fun ReviewTabContent(reviews: List<Review>) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(reviews) { review ->
            val userImageRes = getDrawableResId(context, review.userImageResId) ?: R.drawable.placeholder

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = userImageRes),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = review.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = review.content,
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
            Divider()
        }
    }
}


