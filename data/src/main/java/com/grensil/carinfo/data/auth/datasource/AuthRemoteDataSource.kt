package com.grensil.carinfo.data.auth.datasource

import android.app.Activity
import com.grensil.carinfo.data.auth.dto.FirebaseUserDto
import com.grensil.carinfo.data.auth.model.FirebaseVerificationEvent
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun startVerification(
        phoneNumber: String,
        activity: Activity,
        forceResend: Boolean = false,
    ): Flow<FirebaseVerificationEvent>

    suspend fun verifyCode(smsCode: String): FirebaseUserDto

    fun getCurrentUser(): FirebaseUserDto?
}
