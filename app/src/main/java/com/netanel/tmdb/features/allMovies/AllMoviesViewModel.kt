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

    private val _moviesUiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val moviesUiState: StateFlow<UiState<List<Movie>>> = _moviesUiState.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    private var currentSection: MovieSection.MovieSectionType? = null
    private var currentQuery: String? = null

    fun loadInitial(movieSection: MovieSection.MovieSectionType?, query: String?) {
        currentSection = movieSection
        currentQuery = query

        currentPage = 1
        totalPages = Int.MAX_VALUE

        _isLoadingMore.value = false
        _moviesUiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val response = fetchPageResponse(
                    page = 1,
                    section = currentSection,
                    query = currentQuery
                )

                val movies = response?.movies.orEmpty().distinctBy { it.id }
                totalPages = response?.totalPages ?: 1

                _moviesUiState.value = UiState.Success(movies)
            } catch (e: Exception) {
                _moviesUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    fun loadNextPage() {
        if (_isLoadingMore.value) return
        if (currentPage >= totalPages) return

        val existing = (moviesUiState.value as? UiState.Success)?.data.orEmpty()
        if (existing.isEmpty()) return

        _isLoadingMore.value = true

        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1

                val response = fetchPageResponse(
                    page = nextPage,
                    section = currentSection,
                    query = currentQuery
                )

                val newItems = response?.movies.orEmpty()
                totalPages = response?.totalPages ?: totalPages

                if (newItems.isEmpty()) {
                    currentPage = nextPage // אופציונלי
                    return@launch
                }

                val merged = (existing + newItems).distinctBy { it.id }

                currentPage = nextPage
                _moviesUiState.value = UiState.Success(merged)

            } finally {
                _isLoadingMore.value = false
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
