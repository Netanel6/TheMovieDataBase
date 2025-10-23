package com.netanel.tmdb.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.netanel.tmdb.domain.movie.model.Movie
import com.netanel.tmdb.ui.composables.MovieItem
import com.netanel.tmdb.ui.theme.TMDBTheme

/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = viewModel()
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenContent(
    state: MoviesUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            MoviesUiState.Loading -> {
                CircularProgressIndicator()
            }

            is MoviesUiState.Error -> {
                Text(text = state.message)
            }

            is MoviesUiState.Success -> {
                if (state.movies.isEmpty()) {
                    Text(text = "No movies available right now.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.movies,
                            key = { movie -> movie.id }
                        ) { movie ->
                            MovieItem(movie = movie)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    TMDBTheme {
        HomeScreenContent(state = MoviesUiState.Loading)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun HomeScreenSuccessPreview() {
    TMDBTheme {
        HomeScreenContent(
            state = MoviesUiState.Success(
                movies = listOf(previewMovie, previewMovie.copy(id = 2, title = "Another Movie"))
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenErrorPreview() {
    TMDBTheme {
        HomeScreenContent(
            state = MoviesUiState.Error(message = "Unable to load now playing movies")
        )
    }
}

private val previewMovie = Movie(
    isAdult = false,
    backdropPath = "/7QirCB1o80NEFpQGlQRZerZbQEp.jpg",
    genreIds = listOf(10749, 18),
    id = 1,
    originalLanguage = "es",
    originalTitle = "Culpa nuestra",
    overview = "Jenna and Lion's wedding brings about the long-awaited reunion between Noah and Nick after their breakup.",
    popularity = 1096.6654,
    posterPath = "/yzqHt4m1SeY9FbPrfZ0C2Hi9x1s.jpg",
    releaseDate = "2025-10-15",
    title = "Our Fault",
    isVideo = false,
    voteAverage = 7.854,
    voteCount = 305
)
