package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchBar(
    query: String?,
    onQueryChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
    onViewAllClicked: (MovieSection.MovieSectionType?, String?) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    movies: List<Movie>
) {
    SearchBar(
        query = query ?: "",
        onQueryChange = onQueryChange,
        onSearch = {
            onActiveChange(false)
            onSearchClicked()
        },
        active = active,
        onActiveChange = onActiveChange,
        placeholder = { Text("Search movies...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            columns = GridCells.Fixed(count = 3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieItem(movie = movie, onMovieDetailsClicked = { onMovieClicked(movie) })
            }
            if (query?.isNotEmpty() == true) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = { onViewAllClicked(null, query) }) {
                            Text("View All")
                        }
                    }
                }
            }
        }
    }
}
