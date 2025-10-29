package com.netanel.tmdb.features.movieDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.features.home.UiState

@Composable
fun MovieDetailsScreen(movieId: Int) {
    val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
    val movieDetails = movieDetailsViewModel.movieDetailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        movieDetailsViewModel.getMovieDetails(movieId)
    }

    Column {
        when (val result = movieDetails.value) {
            is UiState.Error -> {
                Text(text = "Error: ${result.message}")
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success<*> -> {
                val data = result.data as MovieDetailsResponse
                Text(text = "Movie Details: ${data.title}")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}


@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieDetailsScreen(movieId = 123)
}