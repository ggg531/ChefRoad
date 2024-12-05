package com.example.chefroad.feature.map

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class MapViewModel : ViewModel() {
    private var naverMap: NaverMap? = null
    private val black = listOf(
        LocationData(
            LatLng(37.501339, 127.122653),
            "조광201"
        ),
        LocationData(
            LatLng(37.630186, 127.041268),
            "깃든"
        )
    )
    private val wednesday = listOf(
        LocationData(
            LatLng(37.539707, 126.886666),
            "길풍식당"
        ),
        LocationData(
            LatLng(37.570125, 126.987034),
            "일미식당"
        )
    )
    private val line = listOf(
        LocationData(
            LatLng(37.5615, 126.9253),
            "바다회사랑"
        ),
        LocationData(
            LatLng(37.539707, 126.886666),
            "몽탄"
        )
    )

    private val markers = mutableListOf<Marker>()
    private val _filterState = MutableLiveData<String?>()
    val filterState: LiveData<String?> = _filterState

    fun initializeMap(map: NaverMap) {
        naverMap = map
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
            filteredLocations.forEach { location ->
                val marker = Marker().apply {
                    position = location.latLng
                    this.map = map
                    captionText = location.caption
                    captionTextSize = 16f
                    isHideCollidedCaptions = false
                    iconTintColor = when (location) {
                        in black -> 0xFF8B00FF.toInt()
                        in wednesday -> 0xFF1E90FF.toInt()
                        in line -> 0xFF32CD32.toInt()
                        else -> Color.WHITE
                    }
                }
                markers.add(marker)
            }
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
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
