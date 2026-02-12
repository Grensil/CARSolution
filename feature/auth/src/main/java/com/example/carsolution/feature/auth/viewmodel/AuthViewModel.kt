package com.example.carsolution.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.core.common.UiState
import com.example.carsolution.domain.model.Vehicle
import com.example.carsolution.domain.usecase.LookupVehicleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val lookupVehicleUseCase: LookupVehicleUseCase,
    ) : ViewModel() {
        private val _vehicleLookup = MutableStateFlow<UiState<Vehicle>>(UiState.Loading)
        val vehicleLookup: StateFlow<UiState<Vehicle>> = _vehicleLookup

        fun lookupVehicle(plateNumber: String) {
            viewModelScope.launch {
                _vehicleLookup.value = UiState.Loading
                try {
                    val vehicle = lookupVehicleUseCase(plateNumber)
                    _vehicleLookup.value = UiState.Success(vehicle)
                } catch (e: NoSuchElementException) {
                    _vehicleLookup.value = UiState.Error(e.message ?: "차량 정보를 찾을 수 없습니다")
                } catch (e: IOException) {
                    _vehicleLookup.value = UiState.Error(e.message ?: "네트워크 오류가 발생했습니다")
                } catch (e: IllegalStateException) {
                    _vehicleLookup.value = UiState.Error(e.message ?: "조회 중 오류 발생")
                }
            }
        }
    }
