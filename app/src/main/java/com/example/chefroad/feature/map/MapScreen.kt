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
import com.example.chefroad.feature.data.model.Category
import com.example.chefroad.feature.data.model.LocationMap
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
                        viewModel.initializeMap(naverMap)
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
            Button(
                onClick = { viewModel.filterMarkers("흑백요리사") },
                modifier = Modifier.padding(4.dp).width(120.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple2,
                    contentColor = Color.White,
                )
            ) {
                Text("흑백요리사")
            }
            Button(
                onClick = { viewModel.filterMarkers("수요미식회") },
                modifier = Modifier.padding(4.dp).width(120.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple2,
                    contentColor = Color.White,
                )
            ) {
                Text("수요미식회")
            }
            Button(
                onClick = { viewModel.filterMarkers("줄 서는 식당") },
                modifier = Modifier.padding(4.dp).width(120.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple2,
                    contentColor = Color.White,
                )
            ) {
                Text("줄 서는 식당")
            }
            Button(
                onClick = { viewModel.filterMarkers(null) },
                modifier = Modifier.padding(4.dp).width(120.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple2,
                    contentColor = Color.White,
                )
            ) {
                Text("전체 보기")
            }
        }
    }
}