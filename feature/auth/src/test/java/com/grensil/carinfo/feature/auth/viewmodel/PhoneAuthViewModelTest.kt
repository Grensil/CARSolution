package com.grensil.carinfo.feature.auth.viewmodel

import app.cash.turbine.test
import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.usecase.SendVerificationCodeUseCase
import com.grensil.carinfo.domain.usecase.VerifyPhoneCodeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PhoneAuthViewModelTest {
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase = mockk()
    private val verifyPhoneCodeUseCase: VerifyPhoneCodeUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 상태는 Idle이다`() =
        runTest {
            // Given & When
            val viewModel = PhoneAuthViewModel(sendVerificationCodeUseCase, verifyPhoneCodeUseCase)

            // Then
            viewModel.uiState.test {
                assertEquals(PhoneAuthUiState.Idle, awaitItem())
            }
        }

    @Test
    fun `인증 코드 검증 성공 시 Verified 상태가 된다`() =
        runTest {
            // Given
            val user = AuthUser(uid = "uid-1", phoneNumber = "+821012345678")
            coEvery { verifyPhoneCodeUseCase("123456") } returns user

            val viewModel = PhoneAuthViewModel(sendVerificationCodeUseCase, verifyPhoneCodeUseCase)

            // When
            viewModel.verifyCode("123456")

            // Then
            viewModel.uiState.test {
                assertEquals(PhoneAuthUiState.Verified, awaitItem())
            }
        }

    @Test
    fun `인증 코드 검증 중 IOException 발생 시 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { verifyPhoneCodeUseCase("000000") } throws IOException("네트워크 오류")

            val viewModel = PhoneAuthViewModel(sendVerificationCodeUseCase, verifyPhoneCodeUseCase)

            // When
            viewModel.verifyCode("000000")

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PhoneAuthUiState.Error)
                assertEquals("네트워크 오류", (state as PhoneAuthUiState.Error).message)
            }
        }

    @Test
    fun `인증 코드가 잘못되면 Error 상태가 된다`() =
        runTest {
            // Given
            coEvery { verifyPhoneCodeUseCase("999999") } throws IllegalArgumentException("잘못된 인증 코드")

            val viewModel = PhoneAuthViewModel(sendVerificationCodeUseCase, verifyPhoneCodeUseCase)

            // When
            viewModel.verifyCode("999999")

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state is PhoneAuthUiState.Error)
                assertEquals("잘못된 인증 코드", (state as PhoneAuthUiState.Error).message)
            }
        }
}
