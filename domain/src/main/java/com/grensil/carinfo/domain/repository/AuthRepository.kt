package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.model.PhoneVerificationEvent
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
