package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.PhoneVerificationEvent
import com.grensil.carinfo.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SendVerificationCodeUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val useCase = SendVerificationCodeUseCase(repository)
    private val fakeActivity = Any()

    @Test
    fun `010 번호를 +82 국제 형식으로 변환하여 호출한다`() =
        runTest {
            // Given
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            // When
            val result = useCase("01012345678", fakeActivity).toList()

            // Then
            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
            verify { repository.requestVerificationCode("+821012345678", fakeActivity, false) }
        }

    @Test
    fun `하이픈이 포함된 번호도 정상 변환한다`() =
        runTest {
            // Given
            every {
                repository.requestVerificationCode("+821098765432", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            // When
            val result = useCase("010-9876-5432", fakeActivity).toList()

            // Then
            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }

    @Test
    fun `82로 시작하는 번호는 +만 붙인다`() =
        runTest {
            // Given
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            // When
            val result = useCase("821012345678", fakeActivity).toList()

            // Then
            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }

    @Test
    fun `forceResend true로 재전송 요청한다`() =
        runTest {
            // Given
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, true)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            // When
            val result = useCase("01012345678", fakeActivity, forceResend = true).toList()

            // Then
            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
            verify { repository.requestVerificationCode("+821012345678", fakeActivity, true) }
        }
}
