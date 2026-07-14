package com.example.codechallenge.echo.presentation

data class EchoUiState(
    val input: String = "",
    val isSubmitting: Boolean = false,
    val submittedName: String? = null,
    val errorMessage: String? = null,
)
