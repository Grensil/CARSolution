package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyPhoneCodeUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        suspend operator fun invoke(smsCode: String): AuthUser = repository.verifyCode(smsCode)
    }
