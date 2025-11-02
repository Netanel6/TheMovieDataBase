package com.netanel.tmdb.features.allMovies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.core.ui.composables.MovieItem
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection.MovieSectionType
import com.netanel.tmdb.domain.models.UiState

/**
 * Created by netanelamar on 01/11/2025.
 * NetanelCA2@gmail.com
 */
@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    sectionType: MovieSectionType = MovieSectionType.DEFAULT,
    onMovieDetailsClicked: (Movie) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val allMoviesViewModel: AllMoviesViewModel = hiltViewModel()
    val movieListState by allMoviesViewModel.moviesUiState.collectAsStateWithLifecycle()

    LaunchedEffect(sectionType) {
        allMoviesViewModel.handleMoviesUiState(sectionType)
    }

    AllMoviesScreenContent(
        modifier = modifier,
        title = sectionType.title,
        state = movieListState,
        onMovieDetailsClicked = onMovieDetailsClicked,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMoviesScreenContent(
    modifier: Modifier = Modifier,
    title: String,
    state: UiState<List<Movie>>,
    onMovieDetailsClicked: (Movie) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

        when (state) {
            is UiState.Error -> {
                Box(
                    modifier = contentModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            UiState.Loading -> {
                Box(
                    modifier = contentModifier,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val movies = state.data
                if (movies.isEmpty()) {
                    Box(
                        modifier = contentModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No movies available",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = contentModifier,
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        items(movies, key = { it.id }) { movie ->
                            MovieItem(movie = movie, onMovieDetailsClicked = onMovieDetailsClicked)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllMoviesScreenPreview() {
    AllMoviesScreenContent(
        title = MovieSectionType.UPCOMING.title,
        state = UiState.Success(
            listOf(
                Movie(
                    isAdult = false,
                    backdropPath = null,
                    genreIds = emptyList(),
                    id = 1,
                    originalLanguage = "en",
                    originalTitle = "Original Title",
                    overview = "Overview",
                    popularity = 0.0,
                    posterPath = null,
                    releaseDate = "2025-01-01",
                    title = "Sample Movie",
                    isVideo = false,
                    voteAverage = 7.5,
                    voteCount = 150
                )
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AllMoviesScreenLoadingPreview() {
    AllMoviesScreenContent(
        title = MovieSectionType.UPCOMING.title,
        state = UiState.Loading,
    )
}

@Preview(showBackground = true)
@Composable
fun AllMoviesScreenErrorPreview() {
    AllMoviesScreenContent(
        title = MovieSectionType.UPCOMING.title,
        state = UiState.Error("Failed to load movies"),
    )
}
