package com.danielvilha.wubba_lubba.features.character

import com.danielvilha.wubba_lubba.GetCharacterDetailQuery

sealed class CharacterDetailUiState {
    object Loading : CharacterDetailUiState()
    data class Success(val character: GetCharacterDetailQuery.Character) : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}
