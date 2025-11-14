package com.netanel.tmdb.features.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.stickyHeader
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMovieDetailsClicked: (Movie) -> Unit,
    onViewAllClicked: (MovieSectionType) -> Unit
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
            kotlinx.coroutines.delay(500)
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
    onViewAllClicked: (MovieSectionType) -> Unit = { },
    query: String = "",
    onQueryChange: (String) -> Unit = { },
    onSearchClicked: () -> Unit = { },
    moviesResults: List<Movie> = emptyList()
) {

    val listState = rememberLazyListState()
    val heroMovie = sections.firstOrNull { it.movieSectionType == MovieSectionType.TOP_RATED }
        ?.state
        ?.let { (it as? UiState.Success)?.data?.maxByOrNull { movie -> movie.voteAverage } }

    val heroMaxHeight = 400.dp
    val density = LocalDensity.current
    val heroMaxHeightPx = with(density) { heroMaxHeight.toPx() }

    val heroHeightPx by remember {
        derivedStateOf {
            val heroItem = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == "hero" }
            when {
                heroItem != null -> {
                    val offset = heroItem.offset.coerceAtLeast(0).toFloat()
                    (heroMaxHeightPx - offset).coerceIn(0f, heroMaxHeightPx)
                }
                listState.firstVisibleItemIndex > 0 -> 0f
                else -> heroMaxHeightPx
            }
        }
    }

    val heroHeightDp = remember(density, heroHeightPx) {
        with(density) { heroHeightPx.toDp() }.coerceIn(0.dp, heroMaxHeight)
    }

    val animatedHeroHeightDp by animateDpAsState(
        targetValue = heroHeightDp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow),
        label = "heroHeight"
    )

    val isHeroVisible by remember {
        derivedStateOf { heroHeightPx > 0f }
    }

    val searchBarTopPadding by animateDpAsState(
        targetValue = if (isHeroVisible) 8.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow),
        label = "searchBarPadding"
    )

    val clampedSearchBarTopPadding = searchBarTopPadding.coerceAtLeast(0.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 150f
                )
            )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item("hero") {
                if (heroMovie != null) {
                    HeroSection(
                        movie = heroMovie,
                        onDetailsClick = onMovieDetailsClicked,
                        modifier = Modifier.height(animatedHeroHeightDp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }

            stickyHeader("search_bar") {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f),
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 4.dp
                ) {
                    MovieSearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearchClicked = onSearchClicked,
                        onMovieClicked = onMovieDetailsClicked,
                        movies = moviesResults,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = clampedSearchBarTopPadding)
                    )
                }
            }

            items(
                items = sections,
                key = { it.movieSectionType }
            ) { section ->
                HorizontalMoviesList(section = section, onViewAllClicked = onViewAllClicked)
            }
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

