package com.netanel.tmdb.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.tmdb.home.MovieSection
import com.netanel.tmdb.home.UiState
import com.netanel.tmdb.ui.composables.MovieItem


@Composable
fun HorizontalMoviesList(section: MovieSection) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = section.title.title,
            style = MaterialTheme.typography.titleLarge
        )

        when (val state = section.state) {
            is UiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is UiState.Error -> Text(
                text = state.message,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )

            is UiState.Success -> LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.data, key = { movie -> movie.id }) { movie ->
                    MovieItem(movie = movie)

                }

            }
        }
    }
}
