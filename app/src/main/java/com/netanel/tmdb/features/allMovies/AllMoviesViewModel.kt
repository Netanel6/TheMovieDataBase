package com.netanel.tmdb.features.allMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieSection
import com.netanel.tmdb.domain.models.UiState
import com.netanel.tmdb.domain.useCase.movie.GetNowPlayingUseCase
import com.netanel.tmdb.domain.useCase.movie.GetPopularUseCase
import com.netanel.tmdb.domain.useCase.movie.GetTopRatedUseCase
import com.netanel.tmdb.domain.useCase.movie.GetUpcomingUseCase
import com.netanel.tmdb.domain.useCase.search.SearchUseCase
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
class AllMoviesViewModel @Inject constructor(
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getTopRatedUseCase: GetTopRatedUseCase,
    private val searchUseCase: SearchUseCase,
) : ViewModel() {

    private val _moviesUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val moviesUiState: StateFlow<UiState<List<Movie>>> = _moviesUiState.asStateFlow()

    fun handleMoviesUiState(movieSection: MovieSection.MovieSectionType?, query: String?) {
        _moviesUiState.value = UiState.Loading
        viewModelScope.launch {
            _moviesUiState.value = try {
                if (movieSection != null) {
                    val movies = when (movieSection) {
                        MovieSection.MovieSectionType.UPCOMING -> getUpcomingUseCase.invoke()?.results.orEmpty()
                        MovieSection.MovieSectionType.NOW_PLAYING -> getNowPlayingUseCase.invoke()?.results.orEmpty()
                        MovieSection.MovieSectionType.TOP_RATED -> getTopRatedUseCase.invoke()?.results.orEmpty()
                        MovieSection.MovieSectionType.POPULAR -> getPopularUseCase.invoke()?.results.orEmpty()
                        MovieSection.MovieSectionType.DEFAULT -> emptyList()
                        null -> emptyList()
                    }
                    UiState.Success(movies)
                } else {
                    val movies = searchUseCase.invoke(query!!)?.results.orEmpty()
                    UiState.Success(movies)
                }


            } catch (error: Exception) {
                UiState.Error(error.message ?: "Unexpected error")
            }
        }
    }
}
