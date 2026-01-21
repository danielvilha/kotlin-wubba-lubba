package com.danielvilha.wubba_lubba.features.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.danielvilha.wubba_lubba.GetCharacterDetailQuery
import com.danielvilha.wubba_lubba.R
import com.danielvilha.wubba_lubba.features.util.ErrorMessage
import com.danielvilha.wubba_lubba.features.util.ProgressIndicator
import com.danielvilha.wubba_lubba.ui.preview.ExcludeFromJacocoGeneratedReport
import com.danielvilha.wubba_lubba.ui.preview.LightDarkPreview
import com.danielvilha.wubba_lubba.ui.theme.WubbaLubbaTheme

@Composable
@LightDarkPreview
@ExcludeFromJacocoGeneratedReport
private fun ScreenPreview(
    @PreviewParameter(CharacterDetailScreenPreview::class)
    value: CharacterDetailUiState
) {
    WubbaLubbaTheme {
        CharacterDetailScreen(
            state = value,
            navController = NavController(LocalContext.current)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    state: CharacterDetailUiState,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.character_detail)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (state) {
                is CharacterDetailUiState.Loading -> {
                    ProgressIndicator()
                }
                is CharacterDetailUiState.Error -> {
                    ErrorMessage(message = state.message)
                }
                is CharacterDetailUiState.Success -> {
                    val char = state.character

                    AsyncImage(
                        model = char.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        item {
                            Text(
                                text = char.name ?: "",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(R.string.species, char.species ?: ""),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(R.string.origin, char.origin?.name ?: ""),
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.episodes),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        items(char.episode) { ep ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Text(
                                    text = "${ep?.episode}: ${ep?.name}",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
class CharacterDetailScreenPreview : PreviewParameterProvider<CharacterDetailUiState> {
    override val values: Sequence<CharacterDetailUiState>
        get() = sequenceOf(
            CharacterDetailUiState.Success(
                character = GetCharacterDetailQuery.Character(
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    origin = GetCharacterDetailQuery.Origin(name = "Earth (C-137)"),
                    episode = listOf(
                        GetCharacterDetailQuery.Episode(
                            episode = "S01E01",
                            name = "Pilot"
                        ),
                        GetCharacterDetailQuery.Episode(
                            episode = "S01E02",
                            name = "Lawnmower Dog"
                        )
                    )
                )
            ),
            CharacterDetailUiState.Loading,
            CharacterDetailUiState.Error("Something went wrong"),
        )
}