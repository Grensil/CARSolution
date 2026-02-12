package com.grensil.carinfo.data.auth.model

import com.grensil.carinfo.data.auth.dto.FirebaseUserDto

sealed interface FirebaseVerificationEvent {
    data object CodeSent : FirebaseVerificationEvent

    data class AutoVerified(
        val userDto: FirebaseUserDto,
    ) : FirebaseVerificationEvent

    data class Failed(
        val message: String,
    ) : FirebaseVerificationEvent
}
