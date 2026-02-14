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
import com.netanel.tmdb.domain.useCase.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val getNowPlayingUseCase: GetNowPlayingUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getTopRatedUseCase: GetTopRatedUseCase,
    private val searchUseCase: SearchUseCase,
) : ViewModel() {

    data class AllMoviesUiState(
        val section: MovieSection.MovieSectionType? = null,
        val query: String? = null,
        val movies: UiState<List<Movie>> = UiState.Loading,
        val isLoadingMore: Boolean = false,
        val currentPage: Int = 1,
        val totalPages: Int = Int.MAX_VALUE
    )

    sealed interface AllMoviesAction {
        data class LoadInitial(
            val section: MovieSection.MovieSectionType?,
            val query: String?
        ) : AllMoviesAction

        data object LoadNextPage : AllMoviesAction
        data object Retry : AllMoviesAction
    }

    private val _uiState = MutableStateFlow(AllMoviesUiState())
    val uiState: StateFlow<AllMoviesUiState> = _uiState.asStateFlow()

    fun onAction(action: AllMoviesAction) {
        when (action) {
            is AllMoviesAction.LoadInitial -> loadInitialInternal(action.section, action.query)
            AllMoviesAction.LoadNextPage -> loadNextPageInternal()
            AllMoviesAction.Retry -> loadInitialInternal(_uiState.value.section, _uiState.value.query)
        }
    }

    private fun loadInitialInternal(section: MovieSection.MovieSectionType?, query: String?) {
        _uiState.update {
            it.copy(
                section = section,
                query = query,
                movies = UiState.Loading,
                isLoadingMore = false,
                currentPage = 1,
                totalPages = Int.MAX_VALUE
            )
        }

        viewModelScope.launch {
            runCatching {
                fetchPageResponse(page = 1, section = section, query = query)
            }.onSuccess { response ->
                val movies = response?.movies.orEmpty().distinctBy { it.id }
                val totalPages = response?.totalPages ?: 1

                _uiState.update {
                    it.copy(
                        movies = UiState.Success(movies),
                        currentPage = 1,
                        totalPages = totalPages
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(movies = UiState.Error(e.message ?: "Unexpected error")) }
            }
        }
    }

    private fun loadNextPageInternal() {
        val state = _uiState.value
        if (state.isLoadingMore) return
        if (state.currentPage >= state.totalPages) return

        val existing = (state.movies as? UiState.Success)?.data.orEmpty()
        if (existing.isEmpty()) return

        _uiState.update { it.copy(isLoadingMore = true) }

        viewModelScope.launch {
            try {
                val nextPage = state.currentPage + 1

                val response = fetchPageResponse(
                    page = nextPage,
                    section = state.section,
                    query = state.query
                )

                val newItems = response?.movies.orEmpty()
                val newTotalPages = response?.totalPages ?: state.totalPages

                if (newItems.isEmpty()) {
                    // keep page as-is; no need to advance if nothing arrived
                    _uiState.update { it.copy(totalPages = newTotalPages) }
                    return@launch
                }

                val merged = (existing + newItems).distinctBy { it.id }

                _uiState.update {
                    it.copy(
                        movies = UiState.Success(merged),
                        currentPage = nextPage,
                        totalPages = newTotalPages
                    )
                }
            } catch (e: Exception) {
                // Important: don’t swallow paging errors silently
                _uiState.update { it.copy(movies = UiState.Error(e.message ?: "Unexpected error")) }
            } finally {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    private suspend fun fetchPageResponse(
        page: Int,
        section: MovieSection.MovieSectionType?,
        query: String?
    ): MovieResponse? {
        return if (section != null) {
            when (section) {
                MovieSection.MovieSectionType.UPCOMING -> getUpcomingUseCase(page)
                MovieSection.MovieSectionType.NOW_PLAYING -> getNowPlayingUseCase(page)
                MovieSection.MovieSectionType.TOP_RATED -> getTopRatedUseCase(page)
                MovieSection.MovieSectionType.POPULAR -> getPopularUseCase(page)
                MovieSection.MovieSectionType.DEFAULT -> null
            }
        } else {
            searchUseCase(query = query.orEmpty(), page = page)
        }
    }
}
