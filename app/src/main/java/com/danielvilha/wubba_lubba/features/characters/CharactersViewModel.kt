package com.danielvilha.wubba_lubba.features.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielvilha.wubba_lubba.GetCharactersQuery
import com.danielvilha.wubba_lubba.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    var uiState by mutableStateOf<CharactersUiState>(CharactersUiState.Loading)
        private set

    private var currentPage = 1
    private var canPaginate = true
    private var fullCharactersList = mutableListOf<GetCharactersQuery.Result>()
    private var searchQuery = TextFieldValue("")
    private var searchJob: Job? = null

    init {
        loadCharacters()
    }

    fun onEvent(event: CharactersUiEvent) {
        when (event) {
            is CharactersUiEvent.OnQueryChange -> {
                searchQuery = event.query

                val currentState = uiState as? CharactersUiState.Success
                if (currentState != null) {
                    uiState = currentState.copy(searchQuery = searchQuery)
                }

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    loadCharacters(isNextPage = false)
                }
            }
            is CharactersUiEvent.OnSearch -> {
                searchQuery = TextFieldValue(event.query)
                currentPage = 1
                fullCharactersList.clear()
                uiState = CharactersUiState.Loading
                loadCharacters(isNextPage = false)
            }
            is CharactersUiEvent.OnClearSearch -> {
                searchQuery = TextFieldValue("")
                currentPage = 1
                fullCharactersList.clear()
                loadCharacters()
            }
            is CharactersUiEvent.LoadNextPage -> {
                loadCharacters(isNextPage = true)
            }
        }
    }

    private fun loadCharacters(isNextPage: Boolean = false) {
        val currentState = uiState as? CharactersUiState.Success
        if (isNextPage && (!canPaginate || currentState?.isFetchingNextPage == true)) return

        viewModelScope.launch {
            if (isNextPage && currentState != null) {
                uiState = currentState.copy(isFetchingNextPage = true)
            } else {
                uiState = CharactersUiState.Loading
                currentPage = 1
                fullCharactersList.clear()
            }

            try {
                val response = repository.getCharactersRaw(
                    page = currentPage,
                    name = searchQuery.text.ifEmpty { null },
                    species = null
                )

                val newCharacters = response?.results?.filterNotNull() ?: emptyList()
                canPaginate = response?.info?.next != null

                if (isNextPage) {
                    fullCharactersList.addAll(newCharacters)
                } else {
                    fullCharactersList = newCharacters.toMutableList()
                }

                uiState = CharactersUiState.Success(
                    characters = fullCharactersList.toList(),
                    searchQuery = searchQuery,
                    isFetchingNextPage = false
                )

                if (canPaginate) currentPage++

            } catch (e: Exception) {
                uiState = CharactersUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
