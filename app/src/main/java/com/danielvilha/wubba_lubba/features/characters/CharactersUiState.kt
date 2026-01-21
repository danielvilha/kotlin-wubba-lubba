package com.danielvilha.wubba_lubba.features.characters

import androidx.compose.ui.text.input.TextFieldValue
import com.danielvilha.wubba_lubba.GetCharactersQuery

sealed class CharactersUiState {
    data class Success(
        val characters: List<GetCharactersQuery.Result?> = emptyList(),
        val searchQuery: TextFieldValue = TextFieldValue(""),
        val isFetchingNextPage: Boolean = false,
    ) : CharactersUiState()
    data class Error(val message: String) : CharactersUiState()
    object Loading : CharactersUiState()
}
