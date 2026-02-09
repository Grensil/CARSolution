package com.example.carsolution.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.domain.model.PhoneVerificationEvent
import com.example.carsolution.domain.usecase.SendVerificationCodeUseCase
import com.example.carsolution.domain.usecase.VerifyPhoneCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val verifyPhoneCodeUseCase: VerifyPhoneCodeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhoneAuthUiState>(PhoneAuthUiState.Idle)
    val uiState: StateFlow<PhoneAuthUiState> = _uiState

    private var verificationJob: Job? = null

    fun sendVerificationCode(phoneNumber: String, activity: Any) {
        startVerification(phoneNumber, activity, forceResend = false)
    }

    fun resendCode(phoneNumber: String, activity: Any) {
        startVerification(phoneNumber, activity, forceResend = true)
    }

    fun verifyCode(smsCode: String) {
        _uiState.value = PhoneAuthUiState.Verifying
        viewModelScope.launch {
            try {
                verifyPhoneCodeUseCase(smsCode)
                _uiState.value = PhoneAuthUiState.Verified
            } catch (e: Exception) {
                _uiState.value = PhoneAuthUiState.Error(
                    message = e.message ?: "인증에 실패했습니다",
                    codeSent = true,
                )
            }
        }
    }

    private fun startVerification(phoneNumber: String, activity: Any, forceResend: Boolean) {
        verificationJob?.cancel()
        _uiState.value = PhoneAuthUiState.SendingCode

        verificationJob = viewModelScope.launch {
            sendVerificationCodeUseCase(phoneNumber, activity, forceResend).collect { event ->
                when (event) {
                    is PhoneVerificationEvent.CodeSent -> {
                        _uiState.value = PhoneAuthUiState.CodeSent
                    }
                    is PhoneVerificationEvent.AutoVerified -> {
                        _uiState.value = PhoneAuthUiState.Verified
                    }
                    is PhoneVerificationEvent.Failed -> {
                        _uiState.value = PhoneAuthUiState.Error(
                            message = event.message,
                            codeSent = _uiState.value is PhoneAuthUiState.CodeSent,
                        )
                    }
                }
            }
        }
    }
}
