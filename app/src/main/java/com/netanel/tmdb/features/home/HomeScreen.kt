package com.netanel.tmdb.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.core.ui.composables.HeroSection
import com.netanel.tmdb.core.ui.composables.HorizontalMoviesList
import com.netanel.tmdb.core.ui.composables.MovieSearchBar
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

    val sections = remember(
        upcomingState,
        nowPlayingState,
        topRatedState,
        popularState,
        onMovieDetailsClicked
    ) {
        listOf(
            MovieSection(MovieSectionType.UPCOMING, upcomingState, onMovieDetailsClicked),
            MovieSection(MovieSectionType.NOW_PLAYING, nowPlayingState, onMovieDetailsClicked),
            MovieSection(MovieSectionType.TOP_RATED, topRatedState, onMovieDetailsClicked),
            MovieSection(MovieSectionType.POPULAR, popularState, onMovieDetailsClicked)
        )
    }

    val searchResults = when (searchResultsState) {
        is UiState.Success -> searchResultsState.data
        else -> emptyList()
    }

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

    val heroScrollProgress by remember(listState) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / 1000f).coerceIn(0f, 1f)
        }
    }

    val animatedHeroProgress by animateFloatAsState(
        targetValue = heroScrollProgress,
        label = "heroScroll"
    )

    val heroMovie = remember(sections) {
        sections
            .firstOrNull { it.movieSectionType == MovieSectionType.TOP_RATED }
            ?.state
            ?.let { it as? UiState.Success }
            ?.data
            ?.maxByOrNull { it.voteAverage }
    }

    MovieSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearchClicked = onSearchClicked,
        onMovieClicked = onMovieDetailsClicked,
        onViewAllClicked = onViewAllClicked,
        movies = moviesResults
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 150f
                )
            ),
        state = listState
    ) {
        item {
            heroMovie?.let {
                HeroSection(
                    movie = it,
                    modifier = Modifier.graphicsLayer {
                        val clampedProgress = animatedHeroProgress.coerceIn(0f, 1f)
                        alpha = (1f - (clampedProgress * 0.35f)).coerceIn(0.6f, 1f)
                        scaleX = 1f - (clampedProgress * 1f)
                        scaleY = 1f - (clampedProgress * 1f)
                        translationY = clampedProgress * -80f
                    }
                ) { clickedMovie ->
                    onMovieDetailsClicked(clickedMovie)
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
