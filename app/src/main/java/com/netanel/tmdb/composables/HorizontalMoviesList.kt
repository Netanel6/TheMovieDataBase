package com.netanel.tmdb.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netanel.tmdb.home.MoviesUiState
import com.netanel.tmdb.ui.composables.MovieItem


@Composable
fun HorizontalMoviesList(state: MoviesUiState.Success) {
    if (state.movies.isEmpty()) {
        Text(text = "No movies available right now.")
    } else {
        Column {
            Text(
                text = "Now Playing 🎬",
                modifier = Modifier
                    .fillMaxWidth(), textAlign = TextAlign.Center
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentPadding = PaddingValues(all = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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