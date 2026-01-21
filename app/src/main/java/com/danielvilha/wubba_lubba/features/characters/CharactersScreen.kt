package com.danielvilha.wubba_lubba.features.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danielvilha.wubba_lubba.GetCharactersQuery
import com.danielvilha.wubba_lubba.features.util.ErrorMessage
import com.danielvilha.wubba_lubba.features.util.ProgressIndicator
import com.danielvilha.wubba_lubba.features.util.WubbaLubbaSearchBar
import com.danielvilha.wubba_lubba.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.wubba_lubba.ui.preview.LightDarkPreview
import com.danielvilha.wubba_lubba.ui.theme.WubbaLubbaTheme

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(CharactersScreenPreview::class)
    value: CharactersUiState
) {
    WubbaLubbaTheme {
        CharactersScreen(
            state = value,
            onCharacterClick = {},
            onEvent = {}
        )
    }
}

@Composable
fun CharactersScreen(
    state: CharactersUiState,
    onEvent: (CharactersUiEvent) -> Unit,
    onCharacterClick: (String) -> Unit,
) {
    val successState = state as? CharactersUiState.Success
    val searchQuery = successState?.searchQuery ?: TextFieldValue("")

    Scaffold(
        topBar = {
            WubbaLubbaSearchBar(
                value = searchQuery,
                results = successState?.characters?.filterNotNull() ?: emptyList(),
                onValueChange = { onEvent(CharactersUiEvent.OnQueryChange(it)) },
                onSearch = { onEvent(CharactersUiEvent.OnSearch(it)) },
                onItemSelected = { character ->
                    onCharacterClick(character.id!!)
                },
                onBackClick = { onEvent(CharactersUiEvent.OnClearSearch) }
            )
        }
    ) { innerPadding ->
        when(state) {
            is CharactersUiState.Loading -> {
                ProgressIndicator()
            }

            is CharactersUiState.Error -> {
                ErrorMessage(message = state.message)
            }

            is CharactersUiState.Success -> {
                val listState = rememberLazyListState()

                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .collect { lastVisibleIndex ->
                            val totalItems = state.characters.size
                            if (lastVisibleIndex != null &&
                                lastVisibleIndex >= totalItems - 5 &&
                                !state.isFetchingNextPage) {
                                onEvent(CharactersUiEvent.LoadNextPage)
                            }
                        }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(state.characters) { character ->
                        CharacterItem(
                            character = character,
                            onCharacterClick = { onCharacterClick(character?.id!!) }
                        )
                    }

                    if (state.isFetchingNextPage) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: GetCharactersQuery.Result?, onCharacterClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onCharacterClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = character?.image,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Column(Modifier.padding(8.dp)) {
                Text(text = character?.name ?: "", style = MaterialTheme.typography.bodyMedium)
                Text(text = character?.species ?: "")
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class CharactersScreenPreview : PreviewParameterProvider<CharactersUiState> {
    override val values: Sequence<CharactersUiState>
        get() = sequenceOf(
            CharactersUiState.Loading,
            CharactersUiState.Error("Something went wrong"),
            CharactersUiState.Success(
                characters = listOf(
                    GetCharactersQuery.Result(
                        id = "1",
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        species = "Human"
                    )
                ),
                searchQuery = TextFieldValue("Rick")
            )
        )
}