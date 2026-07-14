package com.example.codechallenge.echo.domain

/**
 * Abstraction over the (simulated) external service that validates a submitted name.
 * The ViewModel depends on this interface only, so the real network call can be
 * swapped in later without touching presentation code.
 */
interface NameRepository {
    suspend fun submitName(name: String): Result<String>
}

class NameValidationException(message: String) : Exception(message)
