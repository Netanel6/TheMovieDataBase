package com.netanel.tmdb.features.movieDetails

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.domain.models.UiState

@Composable
fun MovieDetailsScreen(movieId: Int) {
    val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
    val movieDetails = movieDetailsViewModel.movieDetailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        movieDetailsViewModel.getMovieDetails(movieId)
    }

    Column {
        when (val result = movieDetails.value) {
            is UiState.Error -> {
                Text(text = "Error: ${result.message}")
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Success<*> -> {
                val data = result.data as MovieDetailsResponse
                MovieDetailsContent(data)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailsContent(data: MovieDetailsResponse) {
    Box(
        Modifier
            .height(400.dp)
    ) {
        GlideImage(
            model = Constants.IMAGES_URL + data.backdropPath,
            contentDescription = data.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 150f
                )
            )
    ) {
        Column(
        ) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                for(genre in data.genres) {
                    Text(
                        fontSize = 12.sp,
                        text = genre.name,
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }
            Text(
                text = data.tagline ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = data.overview,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MovieDetailsScreen(movieId = 278)
}