package com.example.chefroad.feature.map

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chefroad.feature.data.model.LocationMap
import com.example.chefroad.feature.data.model.toLocationMapList
import com.example.chefroad.feature.restaurant.data.loadRestaurants
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var naverMap: NaverMap? = null

    private val _locations = MutableLiveData<List<LocationMap>>()
    val locations: LiveData<List<LocationMap>> get() = _locations

    private val _filterState = MutableLiveData<String?>()
    val filterState: LiveData<String?> get() = _filterState

    private val markers = mutableListOf<Marker>()

    init {
        val restaurants = loadRestaurants(context)
        _locations.value = restaurants.toLocationMapList()
    }

    fun initializeMap(map: NaverMap) {
        naverMap = map
        setupMapSettings()
        addMarkers(null)
    }

    private fun setupMapSettings() {
        naverMap?.apply {
            mapType = NaverMap.MapType.Basic
            moveCamera(CameraUpdate.scrollTo(LatLng(37.5665, 126.9780)))
            /*
            mapType = NaverMap.MapType.Basic
            val boundsBuilder = LatLngBounds.Builder()
            val allLocations = black + wednesday + line
            allLocations.forEach { location ->
                boundsBuilder.include(location.latLng)
            }
            val bounds = boundsBuilder.build()

            moveCamera(CameraUpdate.fitBounds(bounds, 100)) */
        }
    }

    fun addMarkers(filter: String?) {
        clearMarkers()
        val filteredLocations = when (filter) {
            "흑백요리사" -> _locations.value?.filter { it.category.name == "BLACKWHITE" }
            "수요미식회" -> _locations.value?.filter { it.category.name == "WEDNESDAY" }
            "줄 서는 식당" -> _locations.value?.filter { it.category.name == "LINEUP" }
            else -> _locations.value
        }

        naverMap?.let { map ->
            filteredLocations?.forEach { location ->
                val marker = Marker().apply {
                    position = LatLng(location.latitude, location.longitude)
                    this.map = map
                    captionText = location.name
                    /*
                    captionTextSize = 16f
                    isHideCollidedCaptions = false
                    iconTintColor = when (location) {
                        in black -> 0xFF8B00FF.toInt()
                        in wednesday -> 0xFF1E90FF.toInt()
                        in line -> 0xFF32CD32.toInt()
                        else -> Color.WHITE
                    }*/
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
}
