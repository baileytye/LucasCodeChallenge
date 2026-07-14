package com.example.codechallenge.echo.presentation

import com.example.codechallenge.echo.domain.NameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class EchoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before fun setUp() { Dispatchers.setMain(testDispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `onSubmit shows the validated name when the server call succeeds`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.success("Jane Doe")))
        viewModel.onInputChange("Jane Doe")
        viewModel.onSubmit()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Jane Doe", viewModel.uiState.value.submittedName)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `onSubmit shows an error message when the server call fails`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.failure(Exception("Invalid name"))))
        viewModel.onInputChange("123")
        viewModel.onSubmit()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.submittedName)
        assertEquals("Invalid name", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `onSubmit flips isSubmitting while the call is in flight`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.success("Jane"), delayMs = 1_000L))
        viewModel.onInputChange("Jane")
        viewModel.onSubmit()
        testDispatcher.scheduler.runCurrent()
        assertEquals(true, viewModel.uiState.value.isSubmitting)
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `onInputChange clears a previous error`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.failure(Exception("Invalid name"))))
        viewModel.onInputChange("123")
        viewModel.onSubmit()
        testDispatcher.scheduler.advanceUntilIdle()
        check(viewModel.uiState.value.errorMessage != null)
        viewModel.onInputChange("Jane")
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onInputChange updates counterState display text`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.success("Jane")))
        viewModel.onInputChange("Jane")
        assertEquals("4 / 50", viewModel.uiState.value.counterState.displayText)
    }

    @Test
    fun `counterState is reset while submitting`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.success("Jane Doe"), delayMs = 500L))
        viewModel.onInputChange("Jane Doe")
        viewModel.onSubmit()
        assertEquals("0 / 50", viewModel.uiState.value.counterState.displayText)
    }

    @Test
    fun `counterState reflects input length at warning boundary`() {
        val viewModel = EchoViewModel(repository = FakeRepository(Result.success("Jane")))
        viewModel.onInputChange("A".repeat(40))
        assertEquals(40, viewModel.uiState.value.counterState.count)
        assertEquals(true, viewModel.uiState.value.counterState.isWarning)
    }

    private class FakeRepository(
        private val result: Result<String>,
        private val delayMs: Long = 0L,
    ) : NameRepository {
        override suspend fun submitName(name: String): Result<String> {
            if (delayMs > 0) delay(delayMs.milliseconds)
            return result
        }
    }
}
