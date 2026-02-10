package com.example.carsolution.feature.auth.screen

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsolution.feature.auth.viewmodel.PhoneAuthUiState
import com.example.carsolution.feature.auth.viewmodel.PhoneAuthViewModel
import kotlinx.coroutines.delay

private const val TIMER_SECONDS = 120
private const val TIMER_TICK_MS = 1000L
private const val PHONE_NUMBER_MAX_LENGTH = 11
private const val PHONE_NUMBER_MIN_LENGTH = 10
private const val OTP_CODE_LENGTH = 6
private const val TIMER_WARNING_THRESHOLD = 30
private const val SECONDS_PER_MINUTE = 60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneVerificationScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit,
) {
    val viewModel: PhoneAuthViewModel = hiltViewModel()
    val activity = LocalContext.current as Activity
    val uiState by viewModel.uiState.collectAsState()

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var otpCode by rememberSaveable { mutableStateOf("") }
    var remainingSeconds by remember { mutableIntStateOf(0) }

    val codeSent = uiState is PhoneAuthUiState.CodeSent ||
        (uiState is PhoneAuthUiState.Error && (uiState as PhoneAuthUiState.Error).codeSent) ||
        uiState is PhoneAuthUiState.Verifying

    LaunchedEffect(codeSent) {
        if (codeSent) {
            remainingSeconds = TIMER_SECONDS
            while (remainingSeconds > 0) {
                delay(TIMER_TICK_MS)
                remainingSeconds--
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is PhoneAuthUiState.Verified) onVerified()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("본인인증") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
        ) {
            PhoneInputSection(
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                codeSent = codeSent,
                isSending = uiState is PhoneAuthUiState.SendingCode,
                onSendCode = { viewModel.sendVerificationCode(phoneNumber, activity) },
            )

            OtpSection(
                codeSent = codeSent,
                otpCode = otpCode,
                onOtpChange = { otpCode = it },
                remainingSeconds = remainingSeconds,
                isVerifying = uiState is PhoneAuthUiState.Verifying,
                onVerifyCode = { viewModel.verifyCode(otpCode) },
                onResendCode = {
                    otpCode = ""
                    viewModel.resendCode(phoneNumber, activity)
                },
            )

            val errorState = uiState as? PhoneAuthUiState.Error
            if (errorState != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun PhoneInputSection(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    codeSent: Boolean,
    isSending: Boolean,
    onSendCode: () -> Unit,
) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "휴대폰 번호로\n본인인증을 진행합니다",
        style = MaterialTheme.typography.headlineSmall,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "차량 소유자 본인 확인을 위해 필요합니다",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { onPhoneNumberChange(it.filter { c -> c.isDigit() }.take(PHONE_NUMBER_MAX_LENGTH)) },
        label = { Text("휴대폰 번호") },
        placeholder = { Text("01012345678") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth(),
        enabled = !codeSent,
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (!codeSent) {
        Button(
            onClick = onSendCode,
            enabled = phoneNumber.length >= PHONE_NUMBER_MIN_LENGTH && !isSending,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text("인증번호 받기", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun OtpSection(
    codeSent: Boolean,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    remainingSeconds: Int,
    isVerifying: Boolean,
    onVerifyCode: () -> Unit,
    onResendCode: () -> Unit,
) {
    AnimatedVisibility(visible = codeSent) {
        Column {
            OutlinedTextField(
                value = otpCode,
                onValueChange = { onOtpChange(it.filter { c -> c.isDigit() }.take(OTP_CODE_LENGTH)) },
                label = { Text("인증번호 6자리") },
                placeholder = { Text("000000") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { TimerText(remainingSeconds) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onVerifyCode,
                enabled = otpCode.length == OTP_CODE_LENGTH && remainingSeconds > 0 && !isVerifying,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("인증 확인", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onResendCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text("인증번호 재발송")
            }
        }
    }
}

@Composable
private fun TimerText(remainingSeconds: Int) {
    if (remainingSeconds > 0) {
        val min = remainingSeconds / SECONDS_PER_MINUTE
        val sec = remainingSeconds % SECONDS_PER_MINUTE
        Text(
            "남은 시간: $min:${sec.toString().padStart(2, '0')}",
            color = if (remainingSeconds < TIMER_WARNING_THRESHOLD) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    } else {
        Text(
            "인증 시간이 만료되었습니다",
            color = MaterialTheme.colorScheme.error,
        )
    }
}
