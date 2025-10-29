package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.netanel.tmdb.features.home.MovieSection
import com.netanel.tmdb.features.home.UiState


@Composable
fun HorizontalMoviesList(section: MovieSection) {
    when (val state = section.state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Error -> Text(text = state.message)
        is UiState.Success -> {
            Column {
                Text(
                    text = section.title.title,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyRow {
                    items(state.data, key = { movie -> movie.id }) { movie ->
                        MovieItem(
                            movie = movie,
                            onMovieDetailsClicked = { section.onMovieClicked(it) })
                    }
                }
            }
        }
    }
}
