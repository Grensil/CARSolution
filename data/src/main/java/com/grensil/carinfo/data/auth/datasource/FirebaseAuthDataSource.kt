package com.grensil.carinfo.data.auth.datasource

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.grensil.carinfo.data.auth.dto.FirebaseUserDto
import com.grensil.carinfo.data.auth.mapper.toDto
import com.grensil.carinfo.data.auth.model.FirebaseVerificationEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthDataSource
    @Inject
    constructor() : AuthRemoteDataSource {
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

        companion object {
            private const val VERIFICATION_TIMEOUT_SECONDS = 120L
        }

        private var currentVerificationId: String? = null
        private var currentResendToken: PhoneAuthProvider.ForceResendingToken? = null

        override fun startVerification(
            phoneNumber: String,
            activity: Activity,
            forceResend: Boolean,
        ): Flow<FirebaseVerificationEvent> =
            callbackFlow {
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        launch {
                            try {
                                val result = auth.signInWithCredential(credential).await()
                                val userDto = result.user?.toDto()
                                    ?: error("Firebase sign-in succeeded but user is null")
                                trySend(FirebaseVerificationEvent.AutoVerified(userDto))
                            } catch (e: FirebaseException) {
                                trySend(FirebaseVerificationEvent.Failed(mapFirebaseError(e)))
                            } catch (e: IllegalStateException) {
                                trySend(FirebaseVerificationEvent.Failed(e.message ?: "인증에 실패했습니다"))
                            }
                            close()
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        trySend(FirebaseVerificationEvent.Failed(mapFirebaseError(e)))
                        close()
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        currentVerificationId = verificationId
                        currentResendToken = token
                        trySend(FirebaseVerificationEvent.CodeSent)
                    }
                }

                val builder = PhoneAuthOptions
                    .newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(VERIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)

                if (forceResend) {
                    currentResendToken?.let { builder.setForceResendingToken(it) }
                }

                PhoneAuthProvider.verifyPhoneNumber(builder.build())

                awaitClose()
            }

        override suspend fun verifyCode(smsCode: String): FirebaseUserDto {
            val verificationId = currentVerificationId
                ?: error("인증 세션이 만료되었습니다. 다시 요청해주세요.")

            val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
            try {
                val result = auth.signInWithCredential(credential).await()
                return result.user?.toDto()
                    ?: error("Firebase sign-in succeeded but user is null")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                throw IllegalArgumentException("인증번호가 올바르지 않습니다", e)
            } catch (e: FirebaseException) {
                throw IllegalStateException(e.message ?: "인증에 실패했습니다", e)
            }
        }

        override fun getCurrentUser(): FirebaseUserDto? = auth.currentUser?.toDto()

        private fun mapFirebaseError(e: Exception): String =
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> "잘못된 전화번호입니다"
                is FirebaseTooManyRequestsException -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요"
                else -> e.message ?: "인증 요청에 실패했습니다"
            }
    }
