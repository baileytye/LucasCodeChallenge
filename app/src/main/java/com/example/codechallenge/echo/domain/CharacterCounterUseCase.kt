package com.example.codechallenge.echo.domain

/**
 * Computes live character-count metadata for the name input field.
 */
data class CounterState(
    val count: Int,
    val remaining: Int,
    val displayText: String,
    val isWarning: Boolean,
    val isError: Boolean,
)

class CharacterCounterUseCase {

    fun compute(input: String): CounterState {
        val count = input.length
        val remaining = MAX_LENGTH - count
        val displayText = "$count / $MAX_LENGTH"
        return CounterState(
            count = count,
            remaining = remaining,
            displayText = displayText,
            isWarning = count >= WARNING_THRESHOLD,
            isError = count >= MAX_LENGTH,
        ).also { result ->
            lastResult = result
        }
    }

    private var lastResult: CounterState? = null

    private companion object {
        const val MAX_LENGTH = 50
        const val WARNING_THRESHOLD = 40
    }
}
