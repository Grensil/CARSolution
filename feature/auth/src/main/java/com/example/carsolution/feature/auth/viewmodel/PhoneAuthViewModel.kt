package com.example.carsolution.feature.auth.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed interface PhoneAuthUiState {
    data object Idle : PhoneAuthUiState
    data object SendingCode : PhoneAuthUiState
    data object CodeSent : PhoneAuthUiState
    data object Verifying : PhoneAuthUiState
    data object Verified : PhoneAuthUiState
    data class Error(val message: String, val codeSent: Boolean) : PhoneAuthUiState
}

@HiltViewModel
class PhoneAuthViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<PhoneAuthUiState>(PhoneAuthUiState.Idle)
    val uiState: StateFlow<PhoneAuthUiState> = _uiState

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            val message = when (e) {
                is FirebaseAuthInvalidCredentialsException -> "잘못된 전화번호입니다"
                is FirebaseTooManyRequestsException -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요"
                else -> e.message ?: "인증 요청에 실패했습니다"
            }
            _uiState.value = PhoneAuthUiState.Error(
                message = message,
                codeSent = verificationId != null,
            )
        }

        override fun onCodeSent(
            id: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            verificationId = id
            resendToken = token
            _uiState.value = PhoneAuthUiState.CodeSent
        }
    }

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _uiState.value = PhoneAuthUiState.SendingCode

        val formatted = formatKoreanPhone(phoneNumber)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formatted)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendCode(phoneNumber: String, activity: Activity) {
        _uiState.value = PhoneAuthUiState.SendingCode

        val formatted = formatKoreanPhone(phoneNumber)

        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formatted)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        resendToken?.let { builder.setForceResendingToken(it) }

        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    fun verifyCode(smsCode: String) {
        val id = verificationId
        if (id == null) {
            _uiState.value = PhoneAuthUiState.Error(
                message = "인증 세션이 만료되었습니다. 다시 요청해주세요.",
                codeSent = false,
            )
            return
        }

        _uiState.value = PhoneAuthUiState.Verifying
        val credential = PhoneAuthProvider.getCredential(id, smsCode)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        _uiState.value = PhoneAuthUiState.Verifying
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _uiState.value = PhoneAuthUiState.Verified
            }
            .addOnFailureListener { e ->
                val message = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "인증번호가 올바르지 않습니다"
                    else -> e.message ?: "인증에 실패했습니다"
                }
                _uiState.value = PhoneAuthUiState.Error(
                    message = message,
                    codeSent = true,
                )
            }
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
