package com.example.carsolution.feature.fuel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.FuelStation
import com.example.carsolution.domain.repository.FuelStationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FuelHomeViewModel @Inject constructor(
    private val repository: FuelStationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<FuelStation>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<FuelStation>>> = _uiState

    init {
        loadFuelStations()
    }

    private fun loadFuelStations() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val list = repository.getFuelStationList()
                _uiState.value = UiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
