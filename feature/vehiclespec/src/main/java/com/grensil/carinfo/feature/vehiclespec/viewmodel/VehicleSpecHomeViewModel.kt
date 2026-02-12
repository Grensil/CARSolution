package com.grensil.carinfo.feature.vehiclespec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grensil.carinfo.core.common.UiState
import com.grensil.carinfo.domain.model.VehicleSpec
import com.grensil.carinfo.domain.usecase.DecodeVinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VehicleSpecHomeViewModel
    @Inject
    constructor(
        private val decodeVin: DecodeVinUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<VehicleSpec>>(UiState.Loading)
        val uiState: StateFlow<UiState<VehicleSpec>> = _uiState

        init {
            loadDefaultSpec()
        }

        private fun loadDefaultSpec() {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    val spec = decodeVin("1HGBH41JXMN109186")
                    _uiState.value = UiState.Success(spec)
                } catch (e: IOException) {
                    _uiState.value = UiState.Error(e.message ?: "네트워크 오류가 발생했습니다")
                } catch (e: IllegalStateException) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }

        fun searchVin(vin: String) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                try {
                    val spec = decodeVin(vin)
                    _uiState.value = UiState.Success(spec)
                } catch (e: IOException) {
                    _uiState.value = UiState.Error(e.message ?: "네트워크 오류가 발생했습니다")
                } catch (e: IllegalStateException) {
                    _uiState.value = UiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
