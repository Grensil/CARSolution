package com.example.carsolution.feature.usedcar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.UsedCar
import com.example.carsolution.domain.repository.UsedCarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UsedCarHomeViewModel
    @Inject
    constructor(
        private val repository: UsedCarRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<List<UsedCar>>>(UiState.Loading)
        val uiState: StateFlow<UiState<List<UsedCar>>> = _uiState

        init {
            loadUsedCars()
        }

        private fun loadUsedCars() {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    val list = repository.getUsedCarList()
                    _uiState.value = UiState.Success(list)
                } catch (e: IOException) {
                    _uiState.value = UiState.Error(e.message ?: "네트워크 오류가 발생했습니다")
                } catch (e: IllegalStateException) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
