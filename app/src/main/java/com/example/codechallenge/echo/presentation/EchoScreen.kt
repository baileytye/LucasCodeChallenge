package com.example.codechallenge.echo.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codechallenge.R

@Composable
fun EchoScreen(
    modifier: Modifier = Modifier,
    viewModel: EchoViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EchoScreen(
        modifier = modifier,
        uiState = uiState,
        onSubmit = viewModel::onSubmit,
        onInputChange = viewModel::onInputChange,
    )
}

@Composable
fun EchoScreen(
    modifier: Modifier = Modifier,
    uiState: EchoUiState,
    onSubmit: () -> Unit,
    onInputChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = uiState.input,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.your_name)) },
            singleLine = true,
            isError = uiState.errorMessage != null,
        )

        Button(
            onClick = onSubmit,
            enabled = !uiState.isSubmitting,
        ) {
            Text(stringResource(R.string.submit))
        }

        if (uiState.isSubmitting) {
            CircularProgressIndicator()
        }

        uiState.submittedName?.let { submittedName ->
            Text(
                text = stringResource(R.string.name, submittedName),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
