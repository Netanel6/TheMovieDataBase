package com.netanel.tmdb.features.allMovies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.domain.models.MovieResponse
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.domain.models.UiState


/**
 * Created by netanelamar on 01/11/2025.
 * NetanelCA2@gmail.com
 */

@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    sectionType: MovieSection.MovieSectionType = MovieSection.MovieSectionType.DEFAULT
) {
    val allMoviesViewModel: AllMoviesViewModel = hiltViewModel()
    val movieListState = allMoviesViewModel.moviesUiState.collectAsStateWithLifecycle()

    LaunchedEffect(allMoviesViewModel) {
        allMoviesViewModel.handleMoviesUiState(sectionType)
    }

    AllMoviesScreenContent(movieListState.value)
}

@Composable
fun AllMoviesScreenContent(state: UiState<MovieResponse>) {
    Column {
        when (state) {
            is UiState.Error -> Text(state.message)
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success<*> -> {
                val data = state.data as MovieResponse
                LazyColumn {
                    items(data.results.size) { index ->
                        Text(data.results[index].toString())
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AllMoviesScreenPreview() {
    AllMoviesScreen()
}