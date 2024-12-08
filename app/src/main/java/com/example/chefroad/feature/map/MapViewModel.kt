package com.example.chefroad.feature.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chefroad.feature.data.model.LocationMap
import com.example.chefroad.feature.data.model.TvShow
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class MapViewModel : ViewModel() {
    private var naverMap: NaverMap? = null

    private val _locations = MutableLiveData<List<LocationMap>>()
    val locations: LiveData<List<LocationMap>> get() = _locations

    private val _filterState = MutableLiveData<TvShow?>()
    val filterState: LiveData<TvShow?> get() = _filterState

    private val markers = mutableListOf<Marker>()

    fun initializeMap(map: NaverMap) {
        naverMap = map
        if (_locations.value.isNullOrEmpty()) {
            Log.w("MapViewModel", "Locations data not ready. Delaying map setup.")
        } else {
            setupMapSettings()
            addMarkers(null)
        }
    }

    private fun setupMapSettings() {
        naverMap?.apply {
            val boundsBuilder = LatLngBounds.Builder()

            // 데이터가 null이거나 비어 있을 경우 기본 처리
            val allLocations = _locations.value
            if (allLocations.isNullOrEmpty()) {
                moveCamera(CameraUpdate.scrollTo(LatLng(37.5665, 126.9780))) // 기본 서울 좌표
                return
            }

            // LatLngBounds에 좌표 추가
            allLocations.forEach { location ->
                boundsBuilder.include(LatLng(location.latitude, location.longitude))
            }

            // LatLngBounds로 카메라 이동
            val bounds = boundsBuilder.build()
            moveCamera(CameraUpdate.fitBounds(bounds, 100))
        }
    }

    fun addMarkers(filter: TvShow?) {
        clearMarkers()
        val filteredLocations = if (filter == null) {
            _locations.value ?: emptyList()
        } else {
            _locations.value?.filter { it.category == filter } ?: emptyList()
        }

        naverMap?.let { map ->
            filteredLocations.forEach { location ->
                val marker = Marker().apply {
                    position = LatLng(location.latitude, location.longitude)
                    this.map = map
                    captionText = location.name
                    captionTextSize = 16f
                    isHideCollidedCaptions = false
                    iconTintColor = when (location.category) {
                        TvShow.BLACKWHITE -> 0xFF8B00FF.toInt()
                        TvShow.WEDNESDAY -> 0xFF1E90FF.toInt()
                        TvShow.LINEUP -> 0xFF32CD32.toInt()
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

    fun filterMarkers(category: TvShow?) {
        _filterState.value = category
        addMarkers(category)
    }
}
