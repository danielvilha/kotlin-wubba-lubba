package com.danielvilha.wubba_lubba.features.characters

import androidx.compose.ui.text.input.TextFieldValue

sealed class CharactersUiEvent {
    data class OnQueryChange(val query: TextFieldValue) : CharactersUiEvent()
    data class OnSearch(val query: String) : CharactersUiEvent()
    object OnClearSearch : CharactersUiEvent()
    object LoadNextPage : CharactersUiEvent()
}