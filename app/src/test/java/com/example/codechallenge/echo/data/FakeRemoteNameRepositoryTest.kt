package com.example.codechallenge.echo.data

import com.example.codechallenge.echo.domain.NameValidationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakeRemoteNameRepositoryTest {

    @Test
    fun `returns success with the trimmed name when validation passes`() = runTest {
        val repository = FakeRemoteNameRepository(simulatedNetworkDelayMs = 500L)

        val result = repository.submitName("  Jane Doe  ")

        assertEquals(Result.success("Jane Doe"), result)
    }

    @Test
    fun `returns failure with the validation reason when validation fails`() = runTest {
        val repository = FakeRemoteNameRepository(simulatedNetworkDelayMs = 500L)

        val result = repository.submitName("John123")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is NameValidationException)
        assertEquals(
            "Name can only contain letters, spaces, hyphens and apostrophes",
            exception?.message,
        )
    }

    @Test
    fun `returns failure for a blank name`() = runTest {
        val repository = FakeRemoteNameRepository(simulatedNetworkDelayMs = 500L)

        val result = repository.submitName("   ")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NameValidationException)
    }

    @Test
    fun `suspends for the configured simulated network delay`() = runTest {
        val repository = FakeRemoteNameRepository(simulatedNetworkDelayMs = 500L)

        repository.submitName("Jane Doe")

        assertEquals(500L, currentTime)
    }

    @Test
    fun `uses the default delay when none is configured`() = runTest {
        val repository = FakeRemoteNameRepository()

        repository.submitName("Jane Doe")

        assertEquals(700L, currentTime)
    }
}
