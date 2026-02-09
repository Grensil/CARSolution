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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carsolution.feature.auth.viewmodel.PhoneAuthUiState
import com.example.carsolution.feature.auth.viewmodel.PhoneAuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneVerificationScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit,
    viewModel: PhoneAuthViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as Activity
    val uiState by viewModel.uiState.collectAsState()

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var otpCode by rememberSaveable { mutableStateOf("") }
    var remainingSeconds by remember { mutableIntStateOf(0) }

    val codeSent = uiState is PhoneAuthUiState.CodeSent
            || (uiState is PhoneAuthUiState.Error && (uiState as PhoneAuthUiState.Error).codeSent)
            || uiState is PhoneAuthUiState.Verifying

    // Timer
    LaunchedEffect(codeSent) {
        if (codeSent) {
            remainingSeconds = 120
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
            }
        }
    }

    // Navigate on verified
    LaunchedEffect(uiState) {
        if (uiState is PhoneAuthUiState.Verified) {
            onVerified()
        }
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

            // Phone number input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it.filter { c -> c.isDigit() }.take(11) },
                label = { Text("휴대폰 번호") },
                placeholder = { Text("01012345678") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !codeSent,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Send code button
            if (!codeSent) {
                val isSending = uiState is PhoneAuthUiState.SendingCode
                Button(
                    onClick = { viewModel.sendVerificationCode(phoneNumber, activity) },
                    enabled = phoneNumber.length >= 10 && !isSending,
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

            // OTP section
            AnimatedVisibility(visible = codeSent) {
                Column {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it.filter { c -> c.isDigit() }.take(6) },
                        label = { Text("인증번호 6자리") },
                        placeholder = { Text("000000") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        supportingText = {
                            if (remainingSeconds > 0) {
                                val min = remainingSeconds / 60
                                val sec = remainingSeconds % 60
                                Text(
                                    "남은 시간: ${min}:${sec.toString().padStart(2, '0')}",
                                    color = if (remainingSeconds < 30) {
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
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val isVerifying = uiState is PhoneAuthUiState.Verifying
                    Button(
                        onClick = { viewModel.verifyCode(otpCode) },
                        enabled = otpCode.length == 6 && remainingSeconds > 0 && !isVerifying,
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
                        onClick = {
                            otpCode = ""
                            viewModel.resendCode(phoneNumber, activity)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text("인증번호 재발송")
                    }
                }
            }

            // Error message
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
