package com.netanel.tmdb.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.movie.model.Movie
import com.netanel.tmdb.useCase.movie.GetNowPlayingUseCase
import com.netanel.tmdb.useCase.movie.GetPopularUseCase
import com.netanel.tmdb.useCase.movie.GetTopRatedUseCase
import com.netanel.tmdb.useCase.movie.GetUpcomingUseCase
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
class HomeViewModel @Inject constructor(
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
}

/* Data Classes */

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

data class MovieSection(
    val title: String,
    val state: UiState<List<Movie>>
)

/*
sealed class NowPlayingUiState {
    data object Loading : NowPlayingUiState()
    data class Success(val movies: List<Movie>) : NowPlayingUiState()
    data class Error(val message: String) : NowPlayingUiState()
}

sealed class UpcomingUiState {
    data object Loading : UpcomingUiState()
    data class Success(val movies: List<Movie>) : UpcomingUiState()
    data class Error(val message: String) : UpcomingUiState()
}

sealed class TopRatedUiState {
    data object Loading : TopRatedUiState()
    data class Success(val movies: List<Movie>) : TopRatedUiState()
    data class Error(val message: String) : TopRatedUiState()
}

sealed class PopularUiState {
    data object Loading : PopularUiState()
    data class Success(val movies: List<Movie>) : PopularUiState()
    data class Error(val message: String) : PopularUiState()
}*/
