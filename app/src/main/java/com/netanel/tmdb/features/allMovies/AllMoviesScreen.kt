package com.netanel.tmdb.features.allMovies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.core.ui.composables.MovieItem
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection.MovieSectionType
import com.netanel.tmdb.domain.models.UiState
@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    sectionType: MovieSectionType? = null,
    query: String? = null,
    onMovieDetailsClicked: (Movie) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val vm: AllMoviesViewModel = hiltViewModel()

    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(sectionType, query) {
        vm.onAction(AllMoviesViewModel.AllMoviesAction.LoadInitial(sectionType, query))
    }

    AllMoviesScreenContent(
        modifier = modifier,
        title = sectionType?.title ?: (query.orEmpty()),
        state = state.movies,
        isLoadingMore = state.isLoadingMore,
        onLoadMore = { vm.onAction(AllMoviesViewModel.AllMoviesAction.LoadNextPage) },
        onMovieDetailsClicked = onMovieDetailsClicked,
        onNavigateBack = onNavigateBack,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllMoviesScreenContent(
    modifier: Modifier = Modifier,
    title: String,
    state: UiState<List<Movie>>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onMovieDetailsClicked: (Movie) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val gridState = rememberLazyGridState()

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
                Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                    Text(text = state.message, style = MaterialTheme.typography.bodyMedium)
                }
            }

            UiState.Loading -> {
                Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val movies = state.data

                if (movies.isEmpty()) {
                    Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                        Text(text = "No movies available", style = MaterialTheme.typography.bodyMedium)
                    }
                    return@Scaffold
                }

                val shouldLoadMore by remember(gridState, movies.size, isLoadingMore) {
                    derivedStateOf {
                        val layout = gridState.layoutInfo
                        val visibleCount = layout.visibleItemsInfo.size
                        if (visibleCount == 0) return@derivedStateOf false

                        val lastVisible = gridState.firstVisibleItemIndex + visibleCount - 1
                        val threshold = 6
                        lastVisible >= (movies.size - 1 - threshold)
                    }
                }

                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore && !isLoadingMore) onLoadMore()
                }

                LazyVerticalGrid(
                    state = gridState,
                    modifier = contentModifier,
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    // ✅ safer keys: index + id (covers rare duplicate ids too)
                    itemsIndexed(
                        items = movies,
                        key = { index, movie -> "${movie.id}-$index" }
                    ) { _, movie ->
                        MovieItem(movie = movie, onMovieDetailsClicked = onMovieDetailsClicked)
                    }

                    // footer loader
                    if (isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
