package com.example.chefroad.feature.restaurant

import androidx.compose.material3.Text
import androidx.navigation.NavController
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chefroad.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chefroad.feature.model.Restaurant
import com.example.chefroad.feature.restaurant.RestaurantViewModel
import com.example.chefroad.feature.utils.loadRestaurantsFromJson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RestaurantScreen(
    navController: NavHostController,
    selectedTvShow: String,
    restaurantViewModel: RestaurantViewModel = hiltViewModel()
) {


    val filteredIds = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<List<Int>>("filteredIds") // SavedStateHandle에서 id 데이터 가져오기

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Filtered Restaurant IDs:", style = MaterialTheme.typography.headlineMedium)
        if (filteredIds.isNullOrEmpty()) {
            Text(text = "No matching restaurants found.")
        } else {
            filteredIds.forEach { id ->
                Text(text = "Restaurant ID: $id")
            }
        }
    }

    var searchText by remember { mutableStateOf("") }
    var selectedSortOption by remember { mutableStateOf("가까운 순") }
    var expanded by remember { mutableStateOf(false) }

    val restaurants by restaurantViewModel.restaurants.collectAsState()
    val context = LocalContext.current

    // 데이터 로드
    LaunchedEffect(selectedTvShow) {
        restaurantViewModel.loadRestaurants(context)
    }

    // 방송에 맞는 식당만 필터링
    val filteredRestaurants = restaurantViewModel.filterRestaurantsByShow(selectedTvShow)
        .filter { it.name.contains(searchText, ignoreCase = true) }

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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = selectedTvShow,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 검색 및 정렬 UI
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Gray),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text("검색", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )
                }

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(filteredRestaurants) { restaurant ->
                        RestaurantItem(
                            restaurant = restaurant,
                            viewModel = restaurantViewModel,
                            onClick = {
                                navController.navigate("restaurant_detail/${restaurant.id}")
                            }
                        )
                    }
                }
            }
        }
    )
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RestaurantItem(restaurant: Restaurant, viewModel: RestaurantViewModel, onClick: () -> Unit) {
    val isFavorite = viewModel.isFavorite(restaurant)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.restaurant),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(restaurant.name, fontSize = 20.sp)
                Text("거리: ${restaurant.distance}km", fontSize = 14.sp)
            }
        }
        IconButton(onClick = {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val restaurantName = restaurant.name
            viewModel.toggleFavorite(restaurant.id, userId, restaurantName)
        }) {
            Icon(
                painter = if (isFavorite) {
                    painterResource(android.R.drawable.star_on)
                } else {
                    painterResource(android.R.drawable.star_off)
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}





