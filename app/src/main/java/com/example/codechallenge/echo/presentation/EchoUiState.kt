package com.example.codechallenge.echo.presentation

import com.example.codechallenge.echo.domain.CounterState

data class EchoUiState(
    val input: String = "",
    val isSubmitting: Boolean = false,
    val submittedName: String? = null,
    val errorMessage: String? = null,
    val counterState: CounterState = CounterState(
        count = 0,
        remaining = 50,
        displayText = "0 / 50",
        isWarning = false,
        isError = false,
    ),
)
