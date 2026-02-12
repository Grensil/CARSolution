package com.example.carsolution.feature.accident.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.Accident
import com.example.carsolution.domain.usecase.GetAccidentListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AccidentHomeViewModel
    @Inject
    constructor(
        private val getAccidentList: GetAccidentListUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<List<Accident>>>(UiState.Loading)
        val uiState: StateFlow<UiState<List<Accident>>> = _uiState

        init {
            loadAccidents()
        }

        private fun loadAccidents() {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    val list = getAccidentList()
                    _uiState.value = UiState.Success(list)
                } catch (e: IOException) {
                    _uiState.value = UiState.Error(e.message ?: "네트워크 오류가 발생했습니다")
                } catch (e: IllegalStateException) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
