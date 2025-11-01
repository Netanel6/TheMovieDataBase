package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.domain.models.MovieSection.MovieSectionType
import com.netanel.tmdb.domain.models.UiState


@Composable
fun HorizontalMoviesList(section: MovieSection, onViewAllClicked: (MovieSectionType) -> Unit) {
    when (val state = section.state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Error -> Text(text = state.message)
        is UiState.Success -> {
            Column(modifier = Modifier) {
                Row(modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(bottom = 8.dp, top = 16.dp, start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        modifier = Modifier,
                        text = section.movieSectionType.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier.clickable {
                            onViewAllClicked(section.movieSectionType)
                        },
                        text = "View All",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
