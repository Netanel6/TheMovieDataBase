package com.netanel.tmdb.home

import android.util.Log
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */


@Composable
fun HomeScreen(modifier: Modifier) {
    val homeViewModel: HomeViewModel = viewModel()
    val state = homeViewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        when (state.value) {
            is MoviesUiState.Loading -> {
                CircularProgressIndicator()
            }

            is MoviesUiState.Error -> {
                val message = (state.value as MoviesUiState.Error).message
                Log.i("HomeScreen", message)
                Text(text = message)
            }

            is MoviesUiState.Success -> {
                val movies = (state.value as MoviesUiState.Success).movies
                LazyColumn {
                    items(movies.size) { index ->
                        Log.i("home", movies.toString())
                        Text(text = movies[index].title)

                    }
                }
            }

            null -> {}
        }
    }

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(Modifier)
}