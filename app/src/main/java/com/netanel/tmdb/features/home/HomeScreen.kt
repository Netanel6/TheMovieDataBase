package com.netanel.tmdb.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.tmdb.core.ui.composables.HeroSection
import com.netanel.tmdb.core.ui.composables.HorizontalMoviesList
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
fun HomeScreen(modifier: Modifier = Modifier, onMovieDetailsClicked: (Movie) -> Unit, onViewAllClicked: (MovieSectionType) -> Unit) {
    val homeViewModel: MoviesViewModel = hiltViewModel()
    val upcomingState by homeViewModel.upcomingUiState.collectAsStateWithLifecycle()
    val nowPlayingState by homeViewModel.nowPlayingUiState.collectAsStateWithLifecycle()
    val topRatedState by homeViewModel.topRatedUiState.collectAsStateWithLifecycle()
    val popularState by homeViewModel.popularUiState.collectAsStateWithLifecycle()

    val sections = listOf(
        MovieSection(MovieSectionType.UPCOMING, upcomingState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.NOW_PLAYING, nowPlayingState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.TOP_RATED, topRatedState, onMovieDetailsClicked),
        MovieSection(MovieSectionType.POPULAR, popularState, onMovieDetailsClicked)
    )

    HomeScreenContent(
        modifier = modifier,
        sections = sections,
        onMovieDetailsClicked = onMovieDetailsClicked,
        onViewAllClicked = onViewAllClicked,
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    sections: List<MovieSection>,
    onMovieDetailsClicked: (Movie) -> Unit = { },
    onViewAllClicked: (MovieSectionType) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 150f
                )
            )
    ) {
        val heroMovie = sections.firstOrNull { it.movieSectionType == MovieSectionType.TOP_RATED }
            ?.state
            ?.let { (it as? UiState.Success)?.data?.maxByOrNull { movie -> movie.voteAverage } }

        heroMovie?.let {
            HeroSection(it) { clickedMovie ->
                onMovieDetailsClicked(clickedMovie)
            }
        }

        LazyColumn {
            items(sections.size) { index ->
                HorizontalMoviesList(section = sections[index], onViewAllClicked = onViewAllClicked)
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

