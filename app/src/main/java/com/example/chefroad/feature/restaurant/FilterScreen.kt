package com.example.chefroad.feature.restaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chefroad.ui.theme.DarkGray
import com.example.chefroad.ui.theme.Purple1
import com.example.chefroad.ui.theme.Purple2

@Composable
fun FilterScreen(navController: NavController, viewModel: FilterViewModel = viewModel()) {
    var isFilterVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
    ) {
        IconButton(
            onClick = { isFilterVisible = !isFilterVisible },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "filter",
            )
        }

        AnimatedVisibility(
            visible = isFilterVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            FilterContent(
                onApply = {
                    isFilterVisible = false
                    navController.previousBackStackEntry?.savedStateHandle?.set("filters", viewModel.currentFilters.value)
                },
                onCancel = { isFilterVisible = false },
                onReset = { viewModel.resetFilters() }
            )
        }
    }
}

@Composable
fun FilterContent(onApply: () -> Unit, onCancel: () -> Unit, onReset: () -> Unit) {
    val foodTypes = listOf("한식", "양식", "중식", "일식", "비건", "베이커리")
    val resTypes = listOf("뷔페", "파인다이닝", "캐주얼다이닝")
    val moneyRanges= listOf("10,000원~20,000원", "20,000원~30,000원", "30,000원~40,000원", "40,000원~")
    val allergyTypes = listOf("달걀", "우유", "땅콩", "생선", "조개")

    val selectedFoodTypes = remember { mutableStateOf<Set<String>>(setOf()) }
    val selectedResTypes = remember { mutableStateOf<Set<String>>(setOf()) }
    val selectedMoneyRanges = remember { mutableStateOf<Set<String>>(setOf()) }
    val selectedAllergyTypes = remember { mutableStateOf<Set<String>>(setOf()) }

    Surface(
        modifier = Modifier.fillMaxWidth().height(820.dp).padding(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "cancel",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .clickable { onCancel() }
                )

                Text(
                    text = "필터링",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Divider(color = DarkGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "음식 종류", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            val groupedFoodTypes = foodTypes.chunked(3)

            groupedFoodTypes.forEach { group ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    group.forEach { foodType ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (selectedFoodTypes.value.contains(foodType)) Purple1 else Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = Purple1,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedFoodTypes.value = if (selectedFoodTypes.value.contains(foodType)) {
                                        selectedFoodTypes.value - foodType
                                    } else {
                                        selectedFoodTypes.value + foodType
                                    }
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = foodType,
                                color = if (selectedFoodTypes.value.contains(foodType)) Color.White else DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "식당 유형", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                resTypes.forEach { resType ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selectedResTypes.value.contains(resType)) Purple1 else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Purple1,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedResTypes.value = if (selectedResTypes.value.contains(resType)) {
                                    selectedResTypes.value - resType
                                } else {
                                    selectedResTypes.value + resType
                                }
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = resType,
                            color = if (selectedResTypes.value.contains(resType)) Color.White else DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "금액 (메인 메뉴 기준)", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            val groupedMoneyRanges = moneyRanges.chunked(2)

            groupedMoneyRanges.forEach { group ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    group.forEach { moneyRange ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (selectedMoneyRanges.value.contains(moneyRange)) Purple1 else Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = Purple1,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedMoneyRanges.value = if (selectedMoneyRanges.value.contains(moneyRange)) {
                                        selectedMoneyRanges.value - moneyRange
                                    } else {
                                        selectedMoneyRanges.value + moneyRange
                                    }
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = moneyRange,
                                color = if (selectedMoneyRanges.value.contains(moneyRange)) Color.White else DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "알레르기", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                allergyTypes.forEach { allergyType ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selectedAllergyTypes.value.contains(allergyType)) Purple1 else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Purple1,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedAllergyTypes.value = if (selectedAllergyTypes.value.contains(allergyType)) {
                                    selectedAllergyTypes.value - allergyType
                                } else {
                                    selectedAllergyTypes.value + allergyType
                                }
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = allergyType,
                            color = if (selectedAllergyTypes.value.contains(allergyType)) Color.White else DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            selectedFoodTypes.value = setOf()
                            selectedResTypes.value = setOf()
                            selectedMoneyRanges.value = setOf()
                            selectedAllergyTypes.value = setOf()
                            onReset()
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "초기화", color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (selectedFoodTypes.value.isNotEmpty() ||
                                selectedResTypes.value.isNotEmpty() ||
                                selectedMoneyRanges.value.isNotEmpty() ||
                                selectedAllergyTypes.value.isNotEmpty()
                            ) Purple2 else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable(
                            enabled = selectedFoodTypes.value.isNotEmpty() ||
                                    selectedResTypes.value.isNotEmpty() ||
                                    selectedMoneyRanges.value.isNotEmpty() ||
                                    selectedAllergyTypes.value.isNotEmpty()
                        ) { onApply() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "적용",
                        color = if (selectedFoodTypes.value.isNotEmpty() ||
                            selectedResTypes.value.isNotEmpty() ||
                            selectedMoneyRanges.value.isNotEmpty() ||
                            selectedAllergyTypes.value.isNotEmpty()
                        ) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}