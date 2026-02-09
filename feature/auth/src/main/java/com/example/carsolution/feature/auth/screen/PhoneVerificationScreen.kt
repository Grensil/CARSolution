package com.example.carsolution.feature.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 휴대폰 본인인증 화면.
 *
 * 현재는 Fake OTP 방식 (아무 6자리 코드 입력시 통과).
 * 추후 아임포트(PortOne) 본인인증 SDK로 교체 예정.
 *
 * 아임포트 연동 시:
 * 1. build.gradle에 iamport-android SDK 의존성 추가
 * 2. "인증 요청" 버튼 → Iamport.certification() 호출
 * 3. 콜백에서 imp_uid 수신 → 서버에서 인증 결과 검증
 * 4. 성공 시 onVerified() 호출
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneVerificationScreen(
    onBack: () -> Unit,
    onVerified: () -> Unit,
) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var otpCode by rememberSaveable { mutableStateOf("") }
    var otpSent by rememberSaveable { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 타이머
    LaunchedEffect(otpSent, remainingSeconds) {
        if (otpSent && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
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

            // 전화번호 입력
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it.filter { c -> c.isDigit() }.take(11)
                    errorMessage = null
                },
                label = { Text("휴대폰 번호") },
                placeholder = { Text("01012345678") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !otpSent,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!otpSent) {
                Button(
                    onClick = {
                        if (phoneNumber.length < 10) {
                            errorMessage = "올바른 휴대폰 번호를 입력해주세요"
                        } else {
                            // TODO: 아임포트 연동 시 Iamport.certification() 호출로 교체
                            otpSent = true
                            remainingSeconds = 180
                            errorMessage = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text("인증번호 받기", style = MaterialTheme.typography.titleMedium)
                }
            }

            // OTP 입력 영역
            AnimatedVisibility(visible = otpSent) {
                Column {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = {
                            otpCode = it.filter { c -> c.isDigit() }.take(6)
                            errorMessage = null
                        },
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
                                Text("인증 시간이 만료되었습니다", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Fake: 아무 6자리 입력시 통과
                            if (otpCode.length == 6 && remainingSeconds > 0) {
                                onVerified()
                            } else if (remainingSeconds <= 0) {
                                errorMessage = "인증 시간이 만료되었습니다. 다시 요청해주세요."
                            } else {
                                errorMessage = "6자리 인증번호를 입력해주세요"
                            }
                        },
                        enabled = otpCode.length == 6 && remainingSeconds > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Text("인증 확인", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            otpCode = ""
                            remainingSeconds = 180
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text("인증번호 재발송")
                    }
                }
            }

            // 에러 메시지
            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
