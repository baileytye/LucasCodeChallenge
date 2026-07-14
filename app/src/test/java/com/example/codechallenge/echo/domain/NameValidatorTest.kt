package com.example.codechallenge.echo.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NameValidatorTest {

    private val validator = NameValidator()

    @Test
    fun `accepts a simple two-word name`() {
        val result = validator.validate("Jane Doe")

        assertEquals(NameValidationResult.Valid("Jane Doe"), result)
    }

    @Test
    fun `trims surrounding whitespace before validating`() {
        val result = validator.validate("  Jane  ")

        assertEquals(NameValidationResult.Valid("Jane"), result)
    }

    @Test
    fun `accepts accented unicode letters`() {
        val result = validator.validate("José")

        assertEquals(NameValidationResult.Valid("José"), result)
    }

    @Test
    fun `accepts hyphens and apostrophes within a name`() {
        val result = validator.validate("Anne-Marie O'Brien")

        assertEquals(NameValidationResult.Valid("Anne-Marie O'Brien"), result)
    }

    @Test
    fun `rejects a blank input`() {
        val result = validator.validate("   ")

        assertTrue(result is NameValidationResult.Invalid)
    }

    @Test
    fun `rejects a single character name`() {
        val result = validator.validate("A")

        assertTrue(result is NameValidationResult.Invalid)
    }

    @Test
    fun `rejects a name longer than 50 characters`() {
        val tooLong = "A".repeat(51)

        val result = validator.validate(tooLong)

        assertTrue(result is NameValidationResult.Invalid)
    }

    @Test
    fun `rejects digits`() {
        val result = validator.validate("John123")

        assertTrue(result is NameValidationResult.Invalid)
    }

    @Test
    fun `rejects symbols that are not letters, spaces, hyphens or apostrophes`() {
        val result = validator.validate("John@Doe")

        assertTrue(result is NameValidationResult.Invalid)
    }

    @Test
    fun `rejects a name starting with a hyphen`() {
        val result = validator.validate("-John")

        assertTrue(result is NameValidationResult.Invalid)
    }
}
