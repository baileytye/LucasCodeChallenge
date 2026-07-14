package com.example.codechallenge.echo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.echo.data.FakeRemoteNameRepository
import com.example.codechallenge.echo.domain.CharacterCounterUseCase
import com.example.codechallenge.echo.domain.NameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EchoViewModel(
    private val repository: NameRepository = FakeRemoteNameRepository(),
) : ViewModel() {

    private val counterUseCase = CharacterCounterUseCase()

    private val _uiState = MutableStateFlow(EchoUiState())
    val uiState: StateFlow<EchoUiState> = _uiState

    fun onInputChange(newInput: String) {
        val counter = if (newInput != null) counterUseCase.compute(newInput) else counterUseCase.compute("")
        _uiState.update {
            it.copy(
                input = newInput,
                errorMessage = null,
                counterState = counter,
            )
        }
    }

    fun onSubmit() {
        val nameToSubmit = _uiState.value.input
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    counterState = CharacterCounterUseCase().compute(""),
                )
            }
            repository.submitName(nameToSubmit)
                .onSuccess { validatedName ->
                    _uiState.update {
                        it.copy(isSubmitting = false, submittedName = validatedName, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isSubmitting = false, submittedName = null, errorMessage = error.message)
                    }
                }
        }
    }
}
