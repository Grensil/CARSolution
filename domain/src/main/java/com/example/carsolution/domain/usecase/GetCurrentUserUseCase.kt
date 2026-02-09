package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.AuthUser
import com.example.carsolution.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val repository: AuthRepository,
) {
    operator fun invoke(): AuthUser? = repository.getCurrentUser()
}
