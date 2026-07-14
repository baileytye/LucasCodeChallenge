package com.example.codechallenge.echo.domain

/**
 * Result of validating a human name against basic rules.
 */
sealed interface NameValidationResult {
    data class Valid(val normalizedName: String) : NameValidationResult
    data class Invalid(val reason: String) : NameValidationResult
}

/**
 * Pure, synchronous validation of basic human-name rules. Kept separate from any
 * networking concerns so it can be unit tested without coroutines and reused by
 * whichever [NameRepository] implementation stands in for the "server".
 */
class NameValidator {

    fun validate(rawInput: String): NameValidationResult {
        val name = rawInput.trim()
        return when {
            name.isEmpty() ->
                NameValidationResult.Invalid("Please enter a name")
            name.length < MIN_LENGTH ->
                NameValidationResult.Invalid("Name must be at least $MIN_LENGTH characters")
            name.length > MAX_LENGTH ->
                NameValidationResult.Invalid("Name must be at most $MAX_LENGTH characters")
            !NAME_PATTERN.matches(name) ->
                NameValidationResult.Invalid("Name can only contain letters, spaces, hyphens and apostrophes")
            else -> NameValidationResult.Valid(name)
        }
    }

    private companion object {
        const val MIN_LENGTH = 2
        const val MAX_LENGTH = 50

        // Must start and end with a letter; letters, spaces, hyphens and apostrophes in between.
        // \p{L} matches any Unicode letter so accented names (e.g. "José") are accepted.
        val NAME_PATTERN = Regex("^\\p{L}[\\p{L}' -]*\\p{L}$")
    }
}
