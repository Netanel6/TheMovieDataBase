package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.models.Movie


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(movie: Movie, onMovieDetailsClicked: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
            .border(
                border = BorderStroke(0.3.dp, Color.White),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val posterPath = movie.posterPath
            if (posterPath != null) {
                GlideImage(
                    model = Constants.IMAGES_URL + posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier.size(width = 120.dp, height = 180.dp).clickable { onMovieDetailsClicked(movie) },
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 180.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieItem(
        Movie(
            isAdult = false,
            backdropPath = "/7QirCB1o80NEFpQGlQRZerZbQEp.jpg",
            genreIds = listOf(10749, 18),
            id = 1156594,
            originalLanguage = "es",
            originalTitle = "Culpa nuestra",
            overview = "Jenna and Lion's wedding brings about the long-awaited reunion between Noah and Nick after their breakup. Nick's inability to forgive Noah stands as an insurmountable barrier. He, heir to his grandfather's businesses, and she, starting her professional life, resist fueling a flame that's still alive. But now that their paths have crossed again, will love be stronger than resentment?",
            popularity = 1096.6654,
            posterPath = "/yzqHt4m1SeY9FbPrfZ0C2Hi9x1s.jpg",
            releaseDate = "2025-10-15",
            title = "Our Fault",
            isVideo = false,
            voteAverage = 7.854,
            voteCount = 305
        ), {}
    )
}