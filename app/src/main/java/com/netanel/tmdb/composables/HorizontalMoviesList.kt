package com.netanel.tmdb.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.netanel.tmdb.home.MovieSection
import com.netanel.tmdb.home.UiState
import com.netanel.tmdb.ui.composables.MovieItem


@Composable
fun HorizontalMoviesList(section: MovieSection) {
    when (val state = section.state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Error -> Text(text = state.message)
        is UiState.Success -> {
            Column {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyRow {
                    items(state.data, key = { movie -> movie.id }) { movie ->
                        MovieItem(movie = movie)
                    }
                }
            }
        }
    }
}
