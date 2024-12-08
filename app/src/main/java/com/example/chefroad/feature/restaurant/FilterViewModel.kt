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

    fun updateFoodType(type: String?) {
        _currentFilters.value = _currentFilters.value.copy(foodType = type)
    }

    fun updateResType(type: String?) {
        _currentFilters.value = _currentFilters.value.copy(resType = type)
    }

    fun updateMoneyRange(range: Int?) {
        _currentFilters.value = _currentFilters.value.copy(moneyRange = range)
    }

    fun updateAllergyType(type: String?) {
        _currentFilters.value = _currentFilters.value.copy(allergyType = type)
    }
}