package com.netanel.tmdb.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.domain.useCase.movie.GetNowPlayingUseCase
import com.netanel.tmdb.domain.useCase.movie.GetPopularUseCase
import com.netanel.tmdb.domain.useCase.movie.GetTopRatedUseCase
import com.netanel.tmdb.domain.useCase.movie.GetUpcomingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getTopRatedUseCase: GetTopRatedUseCase
) :
    ViewModel() {

    private val _upcomingUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val upcomingUiState: StateFlow<UiState<List<Movie>>> = _upcomingUiState.asStateFlow()

    private val _nowPlayingUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val nowPlayingUiState: StateFlow<UiState<List<Movie>>> = _nowPlayingUiState.asStateFlow()

    private val _topRatedUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val topRatedUiState: StateFlow<UiState<List<Movie>>> = _topRatedUiState.asStateFlow()

    private val _popularUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val popularUiState: StateFlow<UiState<List<Movie>>> = _popularUiState.asStateFlow()


    private val _movieDetails: MutableStateFlow<UiState<MovieDetailsResponse>> =
        MutableStateFlow(UiState.Loading)
    val movieDetails: StateFlow<UiState<MovieDetailsResponse>> = _movieDetails.asStateFlow()

    init {
        loadUpcomingMovies()
        loadNowPlayingMovies()
        loadPopularMovies()
        loadTopRatedMovies()
    }


    private fun loadUpcomingMovies() {
        viewModelScope.launch {
            try {
                val movies = getUpcomingUseCase.invoke()
                _upcomingUiState.value = UiState.Success(
                    data = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _upcomingUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    private fun loadNowPlayingMovies() {
        viewModelScope.launch {
            try {
                val movies = getNowPlayingUseCase.invoke()
                _nowPlayingUiState.value = UiState.Success(
                    data = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _nowPlayingUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    private fun loadTopRatedMovies() {
        viewModelScope.launch {
            try {
                val movies = getTopRatedUseCase.invoke()
                _topRatedUiState.value = UiState.Success(
                    data = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _topRatedUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            try {
                val movies = getPopularUseCase.invoke()
                _popularUiState.value = UiState.Success(
                    data = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _popularUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            try {
                val movies = getUpcomingUseCase.invoke()
                _upcomingUiState.value = UiState.Success(
                    data = movies?.results ?: emptyList()
                )
            } catch (e: Exception) {
                _upcomingUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

}

/* Data Classes */

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class MovieSection(
    val title: MovieSectionType,
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

