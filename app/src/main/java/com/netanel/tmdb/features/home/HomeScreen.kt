package com.netanel.tmdb.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.core.ui.composables.HeroSection
import com.netanel.tmdb.core.ui.composables.HorizontalMoviesList
import com.netanel.tmdb.core.ui.composables.MovieSearchBar
import com.netanel.tmdb.core.ui.composables.MovieItem
import com.netanel.tmdb.core.ui.theme.TMDBTheme
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.domain.models.MovieSection.MovieSectionType
import com.netanel.tmdb.domain.models.UiState
import kotlinx.coroutines.delay


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMovieDetailsClicked: (Movie) -> Unit,
    onViewAllClicked: (MovieSectionType?, String?) -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()

    val upcomingState by homeViewModel.upcomingUiState.collectAsStateWithLifecycle()
    val nowPlayingState by homeViewModel.nowPlayingUiState.collectAsStateWithLifecycle()
    val topRatedState by homeViewModel.topRatedUiState.collectAsStateWithLifecycle()
    val popularState by homeViewModel.popularUiState.collectAsStateWithLifecycle()

    val query = searchViewModel.query.collectAsStateWithLifecycle().value

    val searchResultsState = searchViewModel.searchUiState.collectAsStateWithLifecycle().value
    val searchResults = when (searchResultsState) {
        is UiState.Success -> searchResultsState.data
        else -> emptyList()
    }
    val sections = listOf(
        MovieSection(MovieSectionType.UPCOMING, upcomingState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.NOW_PLAYING, nowPlayingState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.TOP_RATED, topRatedState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.POPULAR, popularState, onMovieDetailsClicked)
    )

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            delay(500)
            searchViewModel.searchMovies()
        }
    }


    HomeScreenContent(
        modifier = modifier,
        sections = sections,
        onMovieDetailsClicked = onMovieDetailsClicked,
        onViewAllClicked = onViewAllClicked,
        query = query,
        onQueryChange = { searchViewModel.onQueryChanged(it) },
        onSearchClicked = { searchViewModel.searchMovies() },
        moviesResults = searchResults
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    sections: List<MovieSection>,
    onMovieDetailsClicked: (Movie) -> Unit = { },
    onViewAllClicked: (MovieSectionType?, String?) -> Unit = { _, _ -> },
    query: String? = null,
    onQueryChange: (String) -> Unit = { },
    onSearchClicked: () -> Unit = { },
    moviesResults: List<Movie> = emptyList()
) {

    val listState = rememberLazyListState()
    var searchActive by remember { mutableStateOf(false) }
    val maxHeroHeight = 400.dp
    val density = LocalDensity.current
    val maxHeroHeightPx = with(density) { maxHeroHeight.toPx() }
    val scrollOffsetPx by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                maxHeroHeightPx
            } else {
                listState.firstVisibleItemScrollOffset.toFloat()
            }
        }
    }
    val targetHeroHeightPx = when {
        searchActive || !query.isNullOrBlank() -> 0f
        else -> (maxHeroHeightPx - scrollOffsetPx).coerceIn(0f, maxHeroHeightPx)
    }
    val targetHeroHeightDp = with(density) { targetHeroHeightPx.toDp() }
    val animatedHeroHeight by animateDpAsState(
        targetValue = targetHeroHeightDp,
        animationSpec = spring(),
        label = "heroHeight"
    )

    LaunchedEffect(query) {
        if (query.isNullOrBlank()) {
            searchActive = false
        }
    }

    val heroMovie = sections.firstOrNull { it.movieSectionType == MovieSectionType.TOP_RATED }
        ?.state
        ?.let { (it as? UiState.Success)?.data?.maxByOrNull { movie -> movie.voteAverage } }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 150f
                )
            )
    ) {
        item {
            heroMovie?.let {
                HeroSection(
                    movie = it,
                    height = animatedHeroHeight
                ) { clickedMovie ->
                    onMovieDetailsClicked(clickedMovie)
                }
            }
        }

        item {
            MovieSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearchClicked = onSearchClicked,
                active = searchActive,
                onActiveChange = { searchActive = it }
            )
        }

        if (searchActive || !query.isNullOrBlank()) {
            item {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    columns = GridCells.Fixed(count = 3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    items(moviesResults, key = { it.id }) { movie ->
                        MovieItem(movie = movie, onMovieDetailsClicked = { onMovieDetailsClicked(movie) })
                    }
                    if (!query.isNullOrBlank()) {
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

        items(sections.size) { index ->
            HorizontalMoviesList(section = sections[index], onViewAllClicked = onViewAllClicked)
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun HomeScreenLoadingPreview() {
    TMDBTheme {
        HomeScreenContent(
            sections = listOf(),
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun HomeScreenSuccessPreview() {
    TMDBTheme {
        HomeScreenContent(
            sections = listOf(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenErrorPreview() {
    TMDBTheme {
        HomeScreenContent(
            sections = listOf(),
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
