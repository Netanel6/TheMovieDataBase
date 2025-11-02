package com.netanel.tmdb.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class MovieSection(
    val movieSectionType: MovieSectionType,
    val state: UiState<List<Movie>>,
    val onMovieClicked: (Movie) -> Unit
) {

    @Parcelize
    enum class MovieSectionType(val title: String): Parcelable {
        UPCOMING("Upcoming"),
        NOW_PLAYING("Now Playing"),
        TOP_RATED("Top Rated"),
        POPULAR("Popular"),
        DEFAULT("Default")
    }
}

