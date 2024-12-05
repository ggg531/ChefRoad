package com.example.chefroad.feature.tvshow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.example.chefroad.R // 패키지에 맞게 수정하세요

@Composable
fun TvShowScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 상단 버튼
        TvShowButton("흑백요리사", R.drawable.blackwhite_logo) {
            navController.navigate("home") // 버튼 클릭 시 HomeScreen으로 이동
        }

        // 중간 버튼
        TvShowButton("수요미식회", R.drawable.wednesday_logo) {
            navController.navigate("home") // 버튼 클릭 시 HomeScreen으로 이동
        }

        // 하단 버튼
        TvShowButton("줄서는식당", R.drawable.lineup_logo) {
            navController.navigate("home") // 버튼 클릭 시 HomeScreen으로 이동
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TvShowButton(text: String, imageRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick, // 버튼 클릭 이벤트
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

