package com.example.chefroad.feature.restaurant

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.chefroad.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import android.util.Log
import com.example.chefroad.feature.model.Restaurant
import com.example.chefroad.feature.utils.loadRestaurantsFromJson
import com.google.firebase.firestore.SetOptions
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> get() = _restaurants

    private val _favoriteRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val favoriteRestaurants: StateFlow<List<Restaurant>> get()= _favoriteRestaurants


    private val _favoriteRestaurantIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteRestaurantIds: StateFlow<Set<Int>> get() = _favoriteRestaurantIds

    init {
        Log.d("RestaurantViewModel", "Initializing ViewModel...")
        observeRestaurantUpdates()
        observeFavoriteIdsUpdates()
        loadFavorites() //원래 이것만 있어야 함
    }

    fun loadRestaurants(context: Context) {
        viewModelScope.launch {
            try {
                _restaurants.value = loadRestaurantsFromJson(context)
                Log.d("loadRestaurants", "Successfully loaded restaurants: ${_restaurants.value}")
            } catch (e: JsonSyntaxException) {
                Log.e("loadRestaurants", "JSON parsing error", e)
            }
        }
    }



    private fun observeRestaurantUpdates() {
        // _restaurants 변경 시 자동으로 favorite 갱신
        _restaurants.onEach {
            Log.d("RestaurantViewModel", "Restaurants updated: $it")
            updateFavoriteRestaurants()
        }.launchIn(viewModelScope)
    }

    private fun observeFavoriteIdsUpdates() {
        // _favoriteRestaurantIds 변경 시 자동으로 favorite 갱신
        _favoriteRestaurantIds.onEach {
            Log.d("RestaurantViewModel", "Favorite IDs updated: $it")
            updateFavoriteRestaurants()
        }.launchIn(viewModelScope)
    }

    private fun updateFavoriteRestaurants() {
        val restaurants = _restaurants.value
        val favoriteIds = _favoriteRestaurantIds.value

        Log.d("RestaurantViewModel", "Updating favorite restaurants...")
        Log.d("RestaurantViewModel", "Current restaurants: $restaurants")
        Log.d("RestaurantViewModel", "Current favorite IDs: $favoriteIds")

        _favoriteRestaurants.value = restaurants.filter { it.id in favoriteIds }
        Log.d("RestaurantViewModel", "Updated favorite restaurants: ${_favoriteRestaurants.value}")
    }

    fun toggleFavorite(restaurantId: Int, userId: String, restaurantName: String) {
        val favoriteRef = FirebaseFirestore.getInstance().collection("favorites")

        // 즐겨찾기 레스토랑을 조회
        favoriteRef.whereEqualTo("restaurantId", restaurantId).whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.documents.isNotEmpty()) {
                // 즐겨찾기 상태가 이미 존재하는 경우, 상태 업데이트
                val document = querySnapshot.documents.first()
                val currentFavoriteStatus = document.getBoolean("favorite") ?: false
                val updatedFavoriteStatus = !currentFavoriteStatus // 상태 반전

                // 문서를 전체 업데이트
                favoriteRef.document(document.id)
                    .set(mapOf(
                        "favorite" to updatedFavoriteStatus,
                        "restaurantId" to restaurantId,
                        "userId" to userId,
                        "restaurantName" to restaurantName
                    ), SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("toggleFavorite", "Favorite status updated to $updatedFavoriteStatus for restaurantId: $restaurantId.")

                        if (updatedFavoriteStatus) {
                            // 즐겨찾기로 추가된 경우
                            _favoriteRestaurantIds.value = _favoriteRestaurantIds.value + restaurantId
                        } else {
                            // 즐겨찾기에서 제거된 경우
                            _favoriteRestaurantIds.value = _favoriteRestaurantIds.value - restaurantId
                        }

                        loadFavorites()
                    }
            } else {
                // 즐겨찾기 레스토랑이 없는 경우, 새로운 레스토랑을 추가
                favoriteRef.add(mapOf(
                    "restaurantId" to restaurantId,
                    "favorite" to true, // 초기값을 true로 설정
                    "userId" to userId, // 유저 아이디 추가
                    "restaurantName" to restaurantName
                )).addOnSuccessListener {
                    Log.d("toggleFavorite", "Favorite added for restaurantId: $restaurantId and userId: $userId")

                    _favoriteRestaurantIds.value = _favoriteRestaurantIds.value + restaurantId
                    loadFavorites()
                }
            }
        }
    }

    private fun loadFavorites() {
        val currentUserId = auth.currentUser?.uid ?: return
        Log.d("RestaurantViewModel", "Loading favorites for user: $currentUserId")
        firestore.collection("favorites")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("RestaurantViewModel", "Error loading favorites", exception)
                    return@addSnapshotListener
                }

                // 데이터가 유효한 경우에만 업데이트
                if (snapshot == null || snapshot.isEmpty) {
                    Log.w("RestaurantViewModel", "No favorites found for user: $currentUserId")
                    _favoriteRestaurantIds.value = emptySet()  // 즐겨찾기 없으면 빈 세트로 설정
                    _favoriteRestaurants.value = emptyList()
                    return@addSnapshotListener
                }

                val favoriteIds = snapshot.documents.mapNotNull {
                    it.getLong("restaurantId")?.toInt()
                }.toSet()

                Log.d("RestaurantViewModel", "Favorite IDs loaded from Firestore: $favoriteIds")
                _favoriteRestaurantIds.value = favoriteIds
                updateFavoriteRestaurants()
            }
    }

    // 즐겨찾기 여부 확인
    fun isFavorite(restaurant: Restaurant): Boolean {
        return _favoriteRestaurantIds.value.contains(restaurant.id)
    }

    // TV 프로그램 이름으로 레스토랑 필터링
    fun filterRestaurantsByShow(tvShow: String): List<Restaurant> {
        return _restaurants.value.filter { it.tvShow.equals(tvShow, ignoreCase = true) }
    }

}



