package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.AuthUser
import com.example.carsolution.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyPhoneCodeUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(smsCode: String): AuthUser = repository.verifyCode(smsCode)
    }
