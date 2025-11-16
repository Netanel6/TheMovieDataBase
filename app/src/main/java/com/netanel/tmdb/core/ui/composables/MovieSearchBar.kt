package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.tmdb.domain.models.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    movies: List<Movie>
) {
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            active = false
            onSearchClicked()
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Search movies...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
       /* LazyColumn {
            items(movies.size, key = { movies[it].id }) {
                MovieItem(movies[it]) { }
            }
        }*/

        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = 140.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieItem(movie = movie, onMovieDetailsClicked = { onMovieClicked(movie) })
            }
        }
    }
}
