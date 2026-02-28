package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.netanel.tmdb.R
import com.netanel.tmdb.core.extension.formatToOneDecimalPlace
import com.netanel.tmdb.core.extension.toImageUrl
import com.netanel.tmdb.domain.models.Movie


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(
    movie: Movie,
    onMovieDetailsClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val posterUrl = movie.posterPath?.toImageUrl()
    val year = movie.releaseDate
    val ratingText = movie.voteAverage.formatToOneDecimalPlace()


    Surface(
        modifier = modifier
            .padding(4.dp)
            .width(140.dp)
            .clickable { onMovieDetailsClicked(movie) },
        shape = shape,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            // Poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(shape)
            ) {
                if (posterUrl != null) {
                    GlideImage(
                        model = posterUrl,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_poster),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }


                // Rating chip (top-right)
                if (ratingText != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "★ $ratingText",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Title + year (bottom)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!year.isNullOrBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = year,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Bottom gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 1f)
                                )
                            )
                        )
                )
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