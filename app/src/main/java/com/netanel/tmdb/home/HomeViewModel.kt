package com.netanel.tmdb.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.movie.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */


sealed class MoviesUiState {
    data object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(private val getNowPlayingUseCase: GetNowPlayingUseCase) :
    ViewModel() {

    private val _uiState: MutableStateFlow<MoviesUiState?> = MutableStateFlow(MoviesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMovies()
    }


    private fun loadMovies() {
        viewModelScope.launch {
            try {
                val movies = getNowPlayingUseCase.invoke()
                _uiState.value = MoviesUiState.Success(
                    movies = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _uiState.value = MoviesUiState.Error(e.message ?: "Unexpected error")
            }
        }
    }
}