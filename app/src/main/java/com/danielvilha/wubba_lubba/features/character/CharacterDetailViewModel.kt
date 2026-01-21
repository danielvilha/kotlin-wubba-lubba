package com.danielvilha.wubba_lubba.features.character

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.wubba_lubba.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var characterId: String = checkNotNull(savedStateHandle["characterId"])

    var uiState by mutableStateOf<CharacterDetailUiState>(CharacterDetailUiState.Loading)
        private set

    init {
        fetchCharacterDetails()
    }

    private fun fetchCharacterDetails() {
        viewModelScope.launch {
            try {
                val character = repository.getCharacterDetail(characterId)
                uiState = if (character != null) {
                    CharacterDetailUiState.Success(character)
                } else {
                    CharacterDetailUiState.Error("Character not found")
                }
            } catch (e: Exception) {
                uiState = CharacterDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}