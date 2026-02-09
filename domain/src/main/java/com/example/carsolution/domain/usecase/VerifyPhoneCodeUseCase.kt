package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.AuthUser
import com.example.carsolution.domain.repository.AuthRepository

class VerifyPhoneCodeUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(smsCode: String): AuthUser =
        repository.verifyCode(smsCode)
}
