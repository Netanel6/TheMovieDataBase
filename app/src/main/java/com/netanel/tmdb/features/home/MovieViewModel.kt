package com.netanel.tmdb.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.domain.models.UiState
import com.netanel.tmdb.domain.useCase.movie.GetNowPlayingUseCase
import com.netanel.tmdb.domain.useCase.movie.GetPopularUseCase
import com.netanel.tmdb.domain.useCase.movie.GetTopRatedUseCase
import com.netanel.tmdb.domain.useCase.movie.GetUpcomingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
) : ViewModel() {

    data class HomeUiState(
        val upcoming: UiState<List<Movie>> = UiState.Loading,
        val nowPlaying: UiState<List<Movie>> = UiState.Loading,
        val topRated: UiState<List<Movie>> = UiState.Loading,
        val popular: UiState<List<Movie>> = UiState.Loading
    )

    sealed interface HomeAction {
        data object LoadAll : HomeAction
        data object RetryUpcoming : HomeAction
        data object RetryNowPlaying : HomeAction
        data object RetryTopRated : HomeAction
        data object RetryPopular : HomeAction
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        onAction(HomeAction.LoadAll)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.LoadAll -> loadAll()
            HomeAction.RetryUpcoming -> loadUpcoming()
            HomeAction.RetryNowPlaying -> loadNowPlaying()
            HomeAction.RetryTopRated -> loadTopRated()
            HomeAction.RetryPopular -> loadPopular()
        }
    }

    private fun loadAll() {
        loadUpcoming()
        loadNowPlaying()
        loadTopRated()
        loadPopular()
    }

    private fun loadUpcoming() = loadSection(
        setLoading = { _uiState.update { it.copy(upcoming = UiState.Loading) } },
        setSuccess = { movies -> _uiState.update { it.copy(upcoming = UiState.Success(movies)) } },
        setError = { msg -> _uiState.update { it.copy(upcoming = UiState.Error(msg)) } },
        loader = { getUpcomingUseCase.invoke(1)?.movies.orEmpty() }
    )

    private fun loadNowPlaying() = loadSection(
        setLoading = { _uiState.update { it.copy(nowPlaying = UiState.Loading) } },
        setSuccess = { movies -> _uiState.update { it.copy(nowPlaying = UiState.Success(movies)) } },
        setError = { msg -> _uiState.update { it.copy(nowPlaying = UiState.Error(msg)) } },
        loader = { getNowPlayingUseCase.invoke(1)?.movies.orEmpty() }
    )

    private fun loadTopRated() = loadSection(
        setLoading = { _uiState.update { it.copy(topRated = UiState.Loading) } },
        setSuccess = { movies -> _uiState.update { it.copy(topRated = UiState.Success(movies)) } },
        setError = { msg -> _uiState.update { it.copy(topRated = UiState.Error(msg)) } },
        loader = { getTopRatedUseCase.invoke(1)?.movies.orEmpty() }
    )

    private fun loadPopular() = loadSection(
        setLoading = { _uiState.update { it.copy(popular = UiState.Loading) } },
        setSuccess = { movies -> _uiState.update { it.copy(popular = UiState.Success(movies)) } },
        setError = { msg -> _uiState.update { it.copy(popular = UiState.Error(msg)) } },
        loader = { getPopularUseCase.invoke(1)?.movies.orEmpty() }
    )

    private fun loadSection(
        setLoading: () -> Unit,
        setSuccess: (List<Movie>) -> Unit,
        setError: (String) -> Unit,
        loader: suspend () -> List<Movie>
    ) {
        viewModelScope.launch {
            setLoading()
            runCatching { loader() }
                .onSuccess(setSuccess)
                .onFailure { setError(it.message ?: "Unexpected error") }
        }
    }
}
