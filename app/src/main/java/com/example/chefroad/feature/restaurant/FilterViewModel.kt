package com.example.chefroad.feature.restaurant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class FilterState(
    val foodType: String? = null,
    val resType: String? = null,
    val moneyRange: Int? = null,
    val allergyType: String? = null,
)

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {
    private val _currentFilters = mutableStateOf(FilterState())
    val currentFilters: State<FilterState> = _currentFilters

    fun resetFilters() {
        _currentFilters.value = FilterState()
    }

    fun updateFilters(foodType: String? = null, resType: String? = null, moneyRange: Int? = null, allergyType: String? = null) {
        _currentFilters.value = _currentFilters.value.copy(
            foodType = foodType ?: _currentFilters.value.foodType,
            resType = resType ?: _currentFilters.value.resType,
            moneyRange = moneyRange ?: _currentFilters.value.moneyRange,
            allergyType = allergyType ?: _currentFilters.value.allergyType
        )
    }
}