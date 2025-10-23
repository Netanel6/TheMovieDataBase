package com.netanel.tmdb.ui.composables

import android.provider.Settings.Global.getString
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.netanel.tmdb.R
import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.movie.model.Movie


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(movie: Movie) {
    Card(Modifier.wrapContentSize()) {
        Column {
            GlideImage(
                model = Constants.IMAGES_URL.plus(movie.posterPath),
                contentDescription = movie.title,
                modifier = Modifier.size(width = 200.dp, height = 300.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp).width(200.dp),
                text = "${movie.title} (${movie.originalTitle})"
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieItem(
        Movie(
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
        )
    )
}