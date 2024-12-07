package com.example.chefroad.feature.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chefroad.feature.data.model.LocationMap
import com.example.chefroad.feature.data.model.TvShow
import com.example.chefroad.feature.restaurant.data.loadLocationMaps
import com.naver.maps.geometry.LatLng
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

    private val _filterState = MutableLiveData<TvShow?>()
    val filterState: LiveData<TvShow?> get() = _filterState

    private val markers = mutableListOf<Marker>()

    init {
        _locations.value = loadLocationMaps(context)
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

    fun addMarkers(filter: TvShow?) {
        clearMarkers()
        val filteredLocations = when (filter) {
            TvShow.BLACKWHITE -> _locations.value?.filter { it.category == TvShow.BLACKWHITE }
            TvShow.WEDNESDAY -> _locations.value?.filter { it.category == TvShow.WEDNESDAY }
            TvShow.LINEUP -> _locations.value?.filter { it.category == TvShow.LINEUP }
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

    fun filterMarkers(category: TvShow?) {
        _filterState.value = category
        addMarkers(category)
    }
}
