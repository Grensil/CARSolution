package com.example.carsolution.domain.usecase

import com.example.carsolution.domain.model.PhoneVerificationEvent
import com.example.carsolution.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendVerificationCodeUseCase
    @Inject
    constructor(
        private val repository: AuthRepository,
    ) {
        operator fun invoke(
            phoneNumber: String,
            activityRef: Any,
            forceResend: Boolean = false,
        ): Flow<PhoneVerificationEvent> {
            val formatted = formatKoreanPhone(phoneNumber)
            return repository.requestVerificationCode(formatted, activityRef, forceResend)
        }

        private fun formatKoreanPhone(phone: String): String {
            val digits = phone.filter { it.isDigit() }
            return if (digits.startsWith("0")) {
                "+82${digits.substring(1)}"
            } else if (digits.startsWith("82")) {
                "+$digits"
            } else {
                "+82$digits"
            }
        }
    }
