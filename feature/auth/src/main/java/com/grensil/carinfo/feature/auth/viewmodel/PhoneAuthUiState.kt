package com.grensil.carinfo.feature.auth.viewmodel

sealed interface PhoneAuthUiState {
    data object Idle : PhoneAuthUiState

    data object SendingCode : PhoneAuthUiState

    data object CodeSent : PhoneAuthUiState

    data object Verifying : PhoneAuthUiState

    data object Verified : PhoneAuthUiState

    data class Error(
        val message: String,
        val codeSent: Boolean,
    ) : PhoneAuthUiState
}
