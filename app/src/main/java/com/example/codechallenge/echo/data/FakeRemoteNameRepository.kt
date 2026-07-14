package com.example.codechallenge.echo.data

import com.example.codechallenge.echo.domain.NameRepository
import com.example.codechallenge.echo.domain.NameValidationException
import com.example.codechallenge.echo.domain.NameValidationResult
import com.example.codechallenge.echo.domain.NameValidator
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Stands in for a real backend call. There is no actual server here: it simply
 * waits to simulate network latency and then applies [NameValidator] as if that
 * logic ran remotely. Swapping this for a real HTTP-backed [NameRepository] later
 * would not require any change to the ViewModel or UI.
 */
class FakeRemoteNameRepository(
    private val validator: NameValidator = NameValidator(),
    private val simulatedNetworkDelayMs: Long = 700L,
) : NameRepository {

    override suspend fun submitName(name: String): Result<String> {
        delay(simulatedNetworkDelayMs.milliseconds)
        return when (val result = validator.validate(name)) {
            is NameValidationResult.Valid -> Result.success(result.normalizedName)
            is NameValidationResult.Invalid -> Result.failure(NameValidationException(result.reason))
        }
    }
}
