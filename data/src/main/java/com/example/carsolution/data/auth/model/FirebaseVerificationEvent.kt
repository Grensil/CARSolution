package com.example.carsolution.data.auth.model

import com.example.carsolution.data.auth.dto.FirebaseUserDto

sealed interface FirebaseVerificationEvent {
    data object CodeSent : FirebaseVerificationEvent
    data class AutoVerified(val userDto: FirebaseUserDto) : FirebaseVerificationEvent
    data class Failed(val message: String) : FirebaseVerificationEvent
}
