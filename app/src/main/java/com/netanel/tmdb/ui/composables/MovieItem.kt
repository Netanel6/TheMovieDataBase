package com.netanel.tmdb.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.netanel.tmdb.domain.movie.model.Movie


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

@Composable
fun MovieItem(movie: Movie) {
    Column { 
        Text(text = movie.title)
        Text(text = movie.overview)
    }
    
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieItem(Movie(
        isAdult = false,
        backdropPath = "/7QirCB1o80NEFpQGlQRZerZbQEp.jpg", // A valid backdrop path
        genreIds = listOf(10749, 18),
        id = 1156594,
        originalLanguage = "es",
        originalTitle = "Culpa nuestra",
        overview = "Jenna and Lion's wedding brings about the long-awaited reunion between Noah and Nick after their breakup. Nick's inability to forgive Noah stands as an insurmountable barrier. He, heir to his grandfather's businesses, and she, starting her professional life, resist fueling a flame that's still alive. But now that their paths have crossed again, will love be stronger than resentment?",
        popularity = 1096.6654,
        posterPath = "/yzqHt4m1SeY9FbPrfZ0C2Hi9x1s.jpg", // A valid poster path
        releaseDate = "2025-10-15", // Date in string format
        title = "Our Fault",
        isVideo = false,
        voteAverage = 7.854,
        voteCount = 305
    ))
}