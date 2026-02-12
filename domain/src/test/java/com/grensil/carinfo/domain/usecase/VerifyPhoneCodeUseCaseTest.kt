package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class VerifyPhoneCodeUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val useCase = VerifyPhoneCodeUseCase(repository)

    @Test
    fun `인증 코드가 올바르면 AuthUser를 반환한다`() =
        runTest {
            // Given
            val expected = AuthUser(uid = "uid-1", phoneNumber = "+821012345678")
            coEvery { repository.verifyCode("123456") } returns expected

            // When
            val result = useCase("123456")

            // Then
            assertEquals(expected, result)
            coVerify(exactly = 1) { repository.verifyCode("123456") }
        }

    @Test(expected = IllegalArgumentException::class)
    fun `인증 코드가 잘못되면 예외를 전파한다`() =
        runTest {
            // Given
            coEvery { repository.verifyCode("000000") } throws IllegalArgumentException("잘못된 인증 코드")

            // When
            useCase("000000")
        }
}
