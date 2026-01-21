package com.danielvilha.wubba_lubba.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Destinations {
    const val CHARACTERS_ROUTE = "characters"
    const val CHARACTER_DETAIL_ROUTE = "characterDetail"
}

@Composable
fun WubbaLubbaNavGraph(destination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = destination
    ) {
        composable(Destinations.CHARACTERS_ROUTE) {
            CharactersRoute(navController)
        }

        composable("${Destinations.CHARACTER_DETAIL_ROUTE}/{characterId}") { entry ->
            val characterId = entry.arguments?.getString("characterId")
            CharacterDetailRoute(
                characterId = characterId ?: "",
                navController = navController,
            )
        }
    }
}