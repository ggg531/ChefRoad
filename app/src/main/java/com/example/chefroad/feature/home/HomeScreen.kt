package com.example.chefroad.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chefroad.feature.restaurant.FilterState
import com.example.chefroad.ui.theme.Purple2

@Composable
fun HomeScreen(navController: NavController) { // 채널 페이지
    Text(text = "채널 페이지")
    var filters by remember { mutableStateOf(FilterState()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
    ) {
        Button(
            onClick = { navController.navigate("filter") },
            modifier = Modifier.align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple2,
                contentColor = Color.White,
            )
        ) {
            Text("방송 채널 선택") // menu icon으로 수정 (TOPBAR 고정)
        }
    }

    val updatedFilters = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<FilterState>("filters", FilterState())
        ?.collectAsState()

    LaunchedEffect(updatedFilters?.value) {
        filters = updatedFilters?.value ?: FilterState()
    }
}