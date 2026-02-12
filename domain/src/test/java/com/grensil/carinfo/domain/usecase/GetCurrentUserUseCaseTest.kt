package com.grensil.carinfo.domain.usecase

import com.grensil.carinfo.domain.model.AuthUser
import com.grensil.carinfo.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetCurrentUserUseCaseTest {
    private val repository: AuthRepository = mockk()
    private val useCase = GetCurrentUserUseCase(repository)

    @Test
    fun `로그인된 유저가 있으면 AuthUser를 반환한다`() {
        // Given
        val expected = AuthUser(uid = "uid-1", phoneNumber = "+821012345678")
        every { repository.getCurrentUser() } returns expected

        // When
        val result = useCase()

        // Then
        assertEquals(expected, result)
        verify(exactly = 1) { repository.getCurrentUser() }
    }

    @Test
    fun `로그인된 유저가 없으면 null을 반환한다`() {
        // Given
        every { repository.getCurrentUser() } returns null

        // When
        val result = useCase()

        // Then
        assertNull(result)
    }
}
