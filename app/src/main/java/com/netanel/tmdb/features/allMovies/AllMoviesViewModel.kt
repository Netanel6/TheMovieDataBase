package com.netanel.tmdb.features.allMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieResponse
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.domain.models.UiState
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
 * Created by netanelamar on 02/11/2025.
 * NetanelCA2@gmail.com
 */
@HiltViewModel
class AllMoviesViewModel @Inject constructor(private val getUpcomingUseCase: GetUpcomingUseCase,
                                             private val getNowPlayingUseCase: GetNowPlayingUseCase,
                                             private val getPopularUseCase: GetPopularUseCase,
                                             private val getTopRatedUseCase: GetTopRatedUseCase
): ViewModel() {

    private val _moviesUiState: MutableStateFlow<UiState<MovieResponse>> =
        MutableStateFlow(UiState.Loading)
    val moviesUiState: StateFlow<UiState<MovieResponse>> = _moviesUiState.asStateFlow()


    fun handleMoviesUiState(movieSection: MovieSection.MovieSectionType) {
     viewModelScope.launch {
         when(movieSection) {
             MovieSection.MovieSectionType.UPCOMING -> _moviesUiState.value = UiState.Success(getUpcomingUseCase.invoke()!!)
             MovieSection.MovieSectionType.NOW_PLAYING -> _moviesUiState.value = UiState.Success(getNowPlayingUseCase.invoke()!!)
             MovieSection.MovieSectionType.TOP_RATED -> _moviesUiState.value = UiState.Success(getTopRatedUseCase.invoke()!!)
             MovieSection.MovieSectionType.POPULAR -> _moviesUiState.value = UiState.Success(getPopularUseCase.invoke()!!)
             MovieSection.MovieSectionType.DEFAULT -> { }
         }
     }
    }
}