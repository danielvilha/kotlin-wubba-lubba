package com.danielvilha.wubba_lubba.ui.route

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.danielvilha.wubba_lubba.features.character.CharacterDetailScreen
import com.danielvilha.wubba_lubba.features.character.CharacterDetailViewModel

@Composable
fun CharacterDetailRoute(
    characterId: String,
    navController: NavHostController,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    viewModel.characterId = characterId

    CharacterDetailScreen(
        state = viewModel.uiState,
        navController = navController
    )
}