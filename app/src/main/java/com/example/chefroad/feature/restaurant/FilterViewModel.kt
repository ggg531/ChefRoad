package com.example.chefroad.feature.restaurant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class FilterState(
    val selectedFoodType: String? = null,
    val selectedResType: String? = null,
    val selectedPriceRange: Int? = null,
    val selectedAllergyType: List<String>? = null
)

class FilterViewModel : ViewModel() {
    private val _currentFilters = MutableLiveData(FilterState())
    val currentFilters: LiveData<FilterState> = _currentFilters

    fun updateFilters(
        foodType: String? = null,
        resType: String? = null,
        priceRange: Int? = null,
        allergyType: List<String>? = null
    ) {
        _currentFilters.value = FilterState(
            selectedFoodType = foodType,
            selectedResType = resType,
            selectedPriceRange = priceRange,
            selectedAllergyType = allergyType
        )
    }

    fun resetFilters() {
        _currentFilters.value = FilterState()
    }
}