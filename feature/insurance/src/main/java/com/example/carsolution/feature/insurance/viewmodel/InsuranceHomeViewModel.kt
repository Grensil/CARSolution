package com.example.carsolution.feature.insurance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.Insurance
import com.example.carsolution.domain.repository.InsuranceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsuranceHomeViewModel @Inject constructor(
    private val repository: InsuranceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Insurance>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Insurance>>> = _uiState

    init {
        loadInsuranceList()
    }

    private fun loadInsuranceList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val list = repository.getInsuranceList()
                _uiState.value = UiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
