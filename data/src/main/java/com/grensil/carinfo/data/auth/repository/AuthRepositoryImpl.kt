package com.grensil.carinfo.data.auth.repository

import android.app.Activity
import com.grensil.carinfo.data.auth.datasource.AuthRemoteDataSource
import com.grensil.carinfo.data.auth.mapper.toEntity
import com.grensil.carinfo.data.auth.model.FirebaseVerificationEvent
import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.model.PhoneVerificationEvent
import com.grensil.carinfo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: AuthRemoteDataSource,
    ) : AuthRepository {
        override fun requestVerificationCode(
            phoneNumber: String,
            activityRef: Any,
            forceResend: Boolean,
        ): Flow<PhoneVerificationEvent> {
            val activity = activityRef as Activity
            return remoteDataSource
                .startVerification(phoneNumber, activity, forceResend)
                .map { event ->
                    when (event) {
                        is FirebaseVerificationEvent.CodeSent -> {
                            PhoneVerificationEvent.CodeSent
                        }

                        is FirebaseVerificationEvent.AutoVerified -> {
                            PhoneVerificationEvent.AutoVerified(event.userDto.toEntity())
                        }

                        is FirebaseVerificationEvent.Failed -> {
                            PhoneVerificationEvent.Failed(event.message)
                        }
                    }
                }
        }

        override suspend fun verifyCode(smsCode: String): AuthUser = remoteDataSource.verifyCode(smsCode).toEntity()

        override fun getCurrentUser(): AuthUser? = remoteDataSource.getCurrentUser()?.toEntity()

        override fun isLoggedIn(): Boolean = remoteDataSource.getCurrentUser() != null
    }
