package com.netanel.tmdb.domain.models


data class MovieSection(
    val movieSectionType: MovieSectionType,
    val state: UiState<List<Movie>>,
    val onMovieClicked: (Movie) -> Unit
) {
    enum class MovieSectionType(val title: String) {
        UPCOMING("Upcoming"),
        NOW_PLAYING("Now Playing"),
        TOP_RATED("Top Rated"),
        POPULAR("Popular")
    }
}

