package com.danielvilha.wubba_lubba

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.input.TextFieldValue
import com.danielvilha.wubba_lubba.features.characters.CharactersScreen
import com.danielvilha.wubba_lubba.features.characters.CharactersUiState
import org.junit.Rule
import org.junit.Test

class CharactersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            CharactersScreen(
                state = CharactersUiState.Loading,
                onEvent = {},
                onCharacterClick = {}
            )
        }
    }

    @Test
    fun testErrorState() {
        val errorMessage = "Something went wrong"
        composeTestRule.setContent {
            CharactersScreen(
                state = CharactersUiState.Error(errorMessage),
                onEvent = {},
                onCharacterClick = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun testSuccessState() {
        val character = GetCharactersQuery.Result(
            id = "1",
            name = "Rick Sanchez",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            species = "Human"
        )
        composeTestRule.setContent {
            CharactersScreen(
                state = CharactersUiState.Success(
                    characters = listOf(character),
                    searchQuery = TextFieldValue("Rick")
                ),
                onEvent = {},
                onCharacterClick = {}
            )
        }

        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Human").assertIsDisplayed()
    }
}