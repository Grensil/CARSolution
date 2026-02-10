package com.example.carsolution.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsolution.domain.model.PhoneVerificationEvent
import com.example.carsolution.domain.usecase.SendVerificationCodeUseCase
import com.example.carsolution.domain.usecase.VerifyPhoneCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private data class VerificationRequest(
    val phoneNumber: String,
    val activity: Any,
    val forceResend: Boolean,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhoneAuthViewModel
    @Inject
    constructor(
        private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
        private val verifyPhoneCodeUseCase: VerifyPhoneCodeUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<PhoneAuthUiState>(PhoneAuthUiState.Idle)
        val uiState: StateFlow<PhoneAuthUiState> = _uiState

        private val verificationRequest = MutableSharedFlow<VerificationRequest>(extraBufferCapacity = 1)

        init {
            viewModelScope.launch {
                verificationRequest
                    .onEach { _uiState.value = PhoneAuthUiState.SendingCode }
                    .flatMapLatest { request ->
                        sendVerificationCodeUseCase(request.phoneNumber, request.activity, request.forceResend)
                    }.collect { event ->
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

        fun sendVerificationCode(
            phoneNumber: String,
            activity: Any,
        ) {
            verificationRequest.tryEmit(VerificationRequest(phoneNumber, activity, forceResend = false))
        }

        fun resendCode(
            phoneNumber: String,
            activity: Any,
        ) {
            verificationRequest.tryEmit(VerificationRequest(phoneNumber, activity, forceResend = true))
        }

        fun verifyCode(smsCode: String) {
            _uiState.value = PhoneAuthUiState.Verifying
            viewModelScope.launch {
                try {
                    verifyPhoneCodeUseCase(smsCode)
                    _uiState.value = PhoneAuthUiState.Verified
                } catch (e: IOException) {
                    _uiState.value = PhoneAuthUiState.Error(
                        message = e.message ?: "네트워크 오류가 발생했습니다",
                        codeSent = true,
                    )
                } catch (e: IllegalArgumentException) {
                    _uiState.value = PhoneAuthUiState.Error(
                        message = e.message ?: "인증에 실패했습니다",
                        codeSent = true,
                    )
                } catch (e: IllegalStateException) {
                    _uiState.value = PhoneAuthUiState.Error(
                        message = e.message ?: "인증에 실패했습니다",
                        codeSent = true,
                    )
                }
            }
        }
    }
