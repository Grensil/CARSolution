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
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("01012345678", fakeActivity).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
            verify { repository.requestVerificationCode("+821012345678", fakeActivity, false) }
        }

    @Test
    fun `하이픈이 포함된 번호도 정상 변환한다`() =
        runTest {
            every {
                repository.requestVerificationCode("+821098765432", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("010-9876-5432", fakeActivity).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }

    @Test
    fun `82로 시작하는 번호는 +만 붙인다`() =
        runTest {
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("821012345678", fakeActivity).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }

    @Test
    fun `이미 +82가 붙은 번호는 숫자만 추출하여 +82를 다시 붙인다`() =
        runTest {
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("+821012345678", fakeActivity).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }

    @Test
    fun `forceResend true로 재전송 요청한다`() =
        runTest {
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, true)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("01012345678", fakeActivity, forceResend = true).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
            verify { repository.requestVerificationCode("+821012345678", fakeActivity, true) }
        }

    @Test
    fun `공백과 특수문자가 포함된 번호에서 숫자만 추출한다`() =
        runTest {
            every {
                repository.requestVerificationCode("+821012345678", fakeActivity, false)
            } returns flowOf(PhoneVerificationEvent.CodeSent)

            val result = useCase("010 1234 5678", fakeActivity).toList()

            assertEquals(listOf(PhoneVerificationEvent.CodeSent), result)
        }
}
