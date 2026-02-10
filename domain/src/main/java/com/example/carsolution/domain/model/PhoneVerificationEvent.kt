package com.example.carsolution.domain.model

sealed interface PhoneVerificationEvent {
    data object CodeSent : PhoneVerificationEvent

    data class AutoVerified(
        val user: AuthUser,
    ) : PhoneVerificationEvent

    data class Failed(
        val message: String,
    ) : PhoneVerificationEvent
}
