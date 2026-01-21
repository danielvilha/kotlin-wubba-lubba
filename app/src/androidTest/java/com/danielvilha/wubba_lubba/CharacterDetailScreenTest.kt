package com.danielvilha.wubba_lubba

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.danielvilha.wubba_lubba.features.character.CharacterDetailScreen
import com.danielvilha.wubba_lubba.features.character.CharacterDetailUiState
import org.junit.Rule
import org.junit.Test

class CharacterDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            CharacterDetailScreen(
                state = CharacterDetailUiState.Loading,
                navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            )
        }
    }

    @Test
    fun testErrorState() {
        val errorMessage = "Something went wrong"
        composeTestRule.setContent {
            CharacterDetailScreen(
                state = CharacterDetailUiState.Error(errorMessage),
                navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun testSuccessState() {
        val character = GetCharacterDetailQuery.Character(
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            origin = GetCharacterDetailQuery.Origin(name = "Earth (C-137)"),
            episode = listOf(
                GetCharacterDetailQuery.Episode(
                    episode = "S01E01",
                    name = "Pilot"
                )
            )
        )

        composeTestRule.setContent {
            CharacterDetailScreen(
                state = CharacterDetailUiState.Success(character),
                navController = TestNavHostController(ApplicationProvider.getApplicationContext())
            )
        }

        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Species: Human").assertIsDisplayed()
        composeTestRule.onNodeWithText("Origin: Earth (C-137)").assertIsDisplayed()
        composeTestRule.onNodeWithText("S01E01: Pilot").assertIsDisplayed()
    }
}