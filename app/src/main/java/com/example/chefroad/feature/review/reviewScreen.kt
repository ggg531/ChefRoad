package com.example.chefroad.feature.review

import androidx.compose.foundation.Image
//import com.example.chefroad.feature.home.HomeViewModel
//import com.example.chefroad.feature.home.SignOutState

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chefroad.R
import com.example.chefroad.feature.model.Review
import com.example.chefroad.ui.theme.PurpleGrey80
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavHostController) {
    val restaurantName = navController.currentBackStackEntry?.arguments?.getString("restaurantName") ?: ""
    val viewModel = hiltViewModel<ReviewViewModel>()
    val reviews= viewModel.reviews.collectAsState()

    val filteredReviews = reviews.value.filter {
        it.restaurantName == restaurantName
    }
    val addReview = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(key1 = Unit) {
        viewModel.getReview()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("$restaurantName 리뷰") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PurpleGrey80)
                .clickable {
                    addReview.value = true
                }
            ) {
                Text(
                    text = "Add Review",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
        },
        containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn {

                items(filteredReviews) { review ->
                    ReviewItem(
                        restaurantName = review.restaurantName,
                        reviewtext = review.content,
                        userId = review.id,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    ) {

                    }
                }
            }
        }

        if (addReview.value) {
            ModalBottomSheet(
                onDismissRequest = { addReview.value = false },
                sheetState = sheetState
            ) {
                AddReviewDialog { content ->
                    viewModel.addReview(restaurantName, content)
                    addReview.value = false
                }
            }
        }
    }
}


@Composable
fun ReviewItem(restaurantName: String, reviewtext: String, userId: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val currentUserId = user?.uid // 현재 사용자 ID

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RandomPersonImage()
        Text(
            text = "Name : $userId\n\nContent: $reviewtext",
            modifier = Modifier.padding(8.dp),
            color = Color.Black,
            style = TextStyle(
                fontSize =20.sp
            ),
        )
    }
}

@Composable
fun AddReviewDialog(onAddReview: (String) -> Unit) {
    var reviewtext by remember {
        mutableStateOf("")
    }
    var restaurantName by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Review")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = reviewtext,
            onValueChange = { reviewtext = it },
            label = { Text("맛있게 드셨다면 리뷰를 남겨주세요!") },
            singleLine = false
        )

        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = {
                onAddReview(reviewtext)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add")
        }
    }
}

@Composable
fun RandomPersonImage() {
    // 이미지 ID들을 리스트에 담기
    val images = listOf(
        R.drawable.random_person1,
        R.drawable.random_person2,
        R.drawable.random_person3,
        R.drawable.random_person4
    )

    // 랜덤으로 하나의 이미지 선택
    val randomImage = images[Random.nextInt(images.size)]

    // 랜덤으로 선택된 이미지 표시
    Image(
        painter = painterResource(id = randomImage),
        contentDescription = "person"
    )
}
