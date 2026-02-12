package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        operator fun invoke(): AuthUser? = repository.getCurrentUser()
    }
