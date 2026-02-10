package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.AuthUser
import com.example.carsolution.domain.model.PhoneVerificationEvent
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun requestVerificationCode(
        phoneNumber: String,
        activityRef: Any,
        forceResend: Boolean = false,
    ): Flow<PhoneVerificationEvent>

    suspend fun verifyCode(smsCode: String): AuthUser

    fun getCurrentUser(): AuthUser?

    fun isLoggedIn(): Boolean
}
