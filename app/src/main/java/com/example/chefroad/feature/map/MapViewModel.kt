package com.example.chefroad.feature.map

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker

class MapViewModel : ViewModel() {
    private var naverMap: NaverMap? = null
    private lateinit var context: Context
    private var currentInfoWindow: InfoWindow? = null
    private val infoWindows = mutableListOf<InfoWindow>()
    private val markers = mutableListOf<Marker>()
    private val black = listOf(
        LocationData(LatLng(37.512345, 127.056789), "트리드"),
        LocationData(LatLng(37.566789, 127.00012), "디핀"),
        LocationData(LatLng(37.543210, 126.987654), "포노 부오노"),
        LocationData(LatLng(37.490000, 127.010000), "CHOI. 쵸이닷"),
        LocationData(LatLng(37.530000, 127.060000), "티엔미미 홍대점"),
        LocationData(LatLng(37.580000, 126.970000), "식당네오"),
        LocationData(LatLng(37.540000, 126.990000), "도량"),
        LocationData(LatLng(37.560000, 127.030000), "비아 톨레도 파스타바"),
        LocationData(LatLng(37.500000, 127.020000), "조광201"),
        LocationData(LatLng(37.570000, 126.980000), "네기스키야키 압구정점"),
        LocationData(LatLng(37.520000, 127.040000), "홍보각"),
        LocationData(LatLng(37.550000, 126.980000), "터치더스카이"),
        LocationData(LatLng(37.580000, 126.990000), "진진"),
        LocationData(LatLng(37.540000, 127.030000), "마마리다이닝"),
        LocationData(LatLng(37.520000, 127.050000), "로컬릿"),
        LocationData(LatLng(37.510000, 127.040000), "본연"),
        LocationData(LatLng(37.560000, 126.970000), "윤서울"),
        LocationData(LatLng(37.500000, 126.950000), "가매일식"),
        LocationData(LatLng(37.510000, 126.960000), "팔공산 엄마밥상 다사세천점"),
        LocationData(LatLng(37.530000, 126.980000), "즐거운술상")
    )
    private val wednesday = listOf(
        LocationData(LatLng(37.52, 126.99), "스위츠플래닛"),
        LocationData(LatLng(37.56, 127.01), "토말 본점"),
        LocationData(LatLng(37.57, 126.96), "파이어벨 코엑스점"),
        LocationData(LatLng(37.54, 127.0), "GOO STK 528"),
        LocationData(LatLng(37.58, 127.05), "스시코마츠"),
        LocationData(LatLng(37.5, 127.04), "엘림"),
        LocationData(LatLng(37.52, 126.97), "라이차이"),
        LocationData(LatLng(37.54, 127.01), "라멘베라보"),
        LocationData(LatLng(37.56, 127.02), "폴앤폴리나"),
        LocationData(LatLng(37.53, 127.06), "톰볼라"),
        LocationData(LatLng(37.57, 126.95), "라싸브어"),
        LocationData(LatLng(37.5, 126.94), "우동가조쿠"),
        LocationData(LatLng(37.55, 126.99), "봉피양 방이점"),
        LocationData(LatLng(37.56, 127.01), "부첼리하우스"),
        LocationData(LatLng(37.58, 126.98), "인도음식점 타지"),
        LocationData(LatLng(37.54, 127.03), "웨스틴 조선 서울 홍연"),
        LocationData(LatLng(37.51, 127.05), "우래옥"),
        LocationData(LatLng(37.5, 127.02), "비야게레로"),
        LocationData(LatLng(37.57, 126.98), "프롬하노이"),
        LocationData(LatLng(37.52, 127.04), "나노하나")
    )
    private val line = listOf(
        LocationData(LatLng(37.539707, 126.886666), "몽탄"),
        LocationData(LatLng(37.540000, 127.030000), "바다회사랑"),
        LocationData(LatLng(37.570000, 126.960000), "웍셔너리"),
        LocationData(LatLng(37.510000, 126.960000), "총각네붕어빵"),
        LocationData(LatLng(37.500000, 127.040000), "온천집"),
        LocationData(LatLng(37.500000, 126.950000), "나노하나"),
        LocationData(LatLng(37.540000, 127.010000), "플롭 안국"),
        LocationData(LatLng(37.570000, 126.950000), "선데이 버거 클럽"),
        LocationData(LatLng(37.620000, 126.900000), "한미옥"),
        LocationData(LatLng(37.560000, 127.020000), "난포"),
        LocationData(LatLng(37.550000, 127.020000), "진성곱창"),
        LocationData(LatLng(37.580000, 127.040000), "소문난 해장국"),
        LocationData(LatLng(37.520000, 126.970000), "피자헤븐"),
        LocationData(LatLng(37.530000, 127.050000), "양반집"),
        LocationData(LatLng(37.570000, 127.020000), "사우스사이드"),
        LocationData(LatLng(37.580000, 126.970000), "스시엔"),
        LocationData(LatLng(37.610000, 126.920000), "브루클린 더 버거 조인트"),
        LocationData(LatLng(37.600000, 127.020000), "라라브레드"),
        LocationData(LatLng(37.520000, 127.030000), "카페 온"),
        LocationData(LatLng(37.590000, 126.940000), "철든김밥")
    )
    private val _filterState = MutableLiveData<String?>()
    val filterState: LiveData<String?> = _filterState

    fun initializeMap(map: NaverMap, context: Context) {
        naverMap = map
        this.context = context
        setupMapSettings()
        addMarkers(null)
    }
    private fun setupMapSettings() {
        naverMap?.apply {
            mapType = NaverMap.MapType.Basic
            val boundsBuilder = LatLngBounds.Builder()
            val allLocations = black + wednesday + line
            allLocations.forEach { location ->
                boundsBuilder.include(location.latLng)
            }
            val bounds = boundsBuilder.build()
            moveCamera(CameraUpdate.fitBounds(bounds, 100))
        }
    }
    fun addMarkers(filter: String?) {
        clearMarkers()
        val filteredLocations = when (filter) {
            "흑백요리사" -> black
            "수요미식회" -> wednesday
            "줄서는식당" -> line
            else -> black + wednesday + line
        }
        naverMap?.let { map ->
            filteredLocations.forEachIndexed { index, location ->
                val marker = Marker().apply {
                    position = location.latLng
                    this.map = map
                    captionText = location.caption
                    captionTextSize = 17f
                    isHideCollidedCaptions = true // 일부 가게명만 보임
                    iconTintColor = when (location) {
                        in black -> 0xFF8B00FF.toInt()
                        in wednesday -> 0xFF1E90FF.toInt()
                        in line -> 0xFF32CD32.toInt()
                        else -> Color.WHITE
                    }
                }
                val infoWindow = InfoWindow().apply {
                    adapter = object : InfoWindow.DefaultTextAdapter(context) {
                        override fun getText(infoWindow: InfoWindow): CharSequence {
                            return location.caption
                        }
                    }
                }
                markers.add(marker)
                infoWindows.add(infoWindow)

                marker.setOnClickListener {
                    if (infoWindow.map != null) {
                        infoWindow.close()
                        currentInfoWindow = null
                    } else {
                        currentInfoWindow?.close()
                        currentInfoWindow = infoWindow
                        infoWindow.open(marker)
                    }
                    true
                }
            }
        }
    }

    private fun getClickHandler(index: Int): () -> Unit {
        return {
            val infoWindow = infoWindows[index]
            val marker = markers[index]
            if (infoWindow.map != null) {
                infoWindow.close()
            } else {
                infoWindow.open(marker)
            }
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
        infoWindows.forEach { it.close() }
        infoWindows.clear()
        currentInfoWindow = null
    }
    fun filterMarkers(category: String?) {
        _filterState.value = category
        addMarkers(category)
    }
    data class LocationData(
        val latLng: LatLng,
        val caption: String
    )
}