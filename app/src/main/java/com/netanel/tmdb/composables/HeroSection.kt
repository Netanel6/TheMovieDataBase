package com.netanel.tmdb.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.movie.model.Movie

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HeroSection(movie: Movie?, onDetailsClick: (movie: Movie) -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        GlideImage(
            model = Constants.IMAGES_URL.plus(movie?.backdropPath),
            contentDescription = movie?.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = movie?.title.toString(),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {

                if (movie?.isVideo == true) {
                    Button(onClick = {
                        Toast.makeText(
                            context,
                            "Need to Implement Video Playing",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text("Play")
                    }
                }
                OutlinedButton(onClick = { onDetailsClick(movie!!) }) {
                    Text("Details")
                }
            }
        }
    }
}
