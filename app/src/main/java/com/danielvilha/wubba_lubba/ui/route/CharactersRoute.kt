package com.danielvilha.wubba_lubba.ui.route

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.danielvilha.wubba_lubba.features.characters.CharactersScreen
import com.danielvilha.wubba_lubba.features.characters.CharactersViewModel

@Composable
fun CharactersRoute(
    navController: NavHostController,
    viewModel: CharactersViewModel = hiltViewModel()
) {

    CharactersScreen(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        onCharacterClick = {
            navController.navigate("${Destinations.CHARACTER_DETAIL_ROUTE}/$it")
        }
    )
}