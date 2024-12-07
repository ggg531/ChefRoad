package com.example.chefroad.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chefroad.feature.data.model.TvShow
import com.example.chefroad.ui.theme.BottomBar
import com.example.chefroad.ui.theme.Purple2
import com.naver.maps.map.MapView

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val locations by viewModel.locations.observeAsState(emptyList())
    val filterState by viewModel.filterState.observeAsState(null)

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    getMapAsync { naverMap ->
                        if (locations.isNotEmpty()) {
                            viewModel.initializeMap(naverMap)
                        }
                    }
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BottomBar)
                .padding(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterButton(
                label = "흑백요리사",
                isSelected = filterState == TvShow.BLACKWHITE,
                onClick = { viewModel.filterMarkers(TvShow.BLACKWHITE) }
            )
            FilterButton(
                label = "수요미식회",
                isSelected = filterState == TvShow.WEDNESDAY,
                onClick = { viewModel.filterMarkers(TvShow.WEDNESDAY) }
            )
            FilterButton(
                label = "줄 서는 식당",
                isSelected = filterState == TvShow.LINEUP,
                onClick = { viewModel.filterMarkers(TvShow.LINEUP) }
            )
            FilterButton(
                label = "전체 보기",
                isSelected = filterState == null,
                onClick = { viewModel.filterMarkers(null) }
            )
        }
    }
}

@Composable
fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Purple2 else Color.LightGray,
            contentColor = Color.White
        )
    ) {
        Text(label)
    }
}