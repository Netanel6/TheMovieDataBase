package com.netanel.tmdb.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.Movie
import com.netanel.tmdb.domain.models.UiState
import com.netanel.tmdb.domain.useCase.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    data class SearchUiState(
        val query: String = "",
        val results: UiState<List<Movie>> = UiState.Success(emptyList())
    )

    sealed interface SearchAction {
        data class QueryChanged(val value: String) : SearchAction
        data object SearchClicked : SearchAction
        data object Clear : SearchAction
    }

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> onQueryChangedInternal(action.value)
            SearchAction.SearchClicked -> searchMoviesInternal()
            SearchAction.Clear -> clearInternal()
        }
    }

    private fun onQueryChangedInternal(newQuery: String) {
        _uiState.update { state ->
            val trimmed = newQuery
            if (trimmed.isBlank()) state.copy(query = trimmed, results = UiState.Success(emptyList()))
            else state.copy(query = trimmed)
        }
    }

    private fun clearInternal() {
        _uiState.value = SearchUiState()
    }

    private fun searchMoviesInternal() {
        viewModelScope.launch {
            val query = _uiState.value.query.trim()
            if (query.isEmpty()) {
                _uiState.update { it.copy(results = UiState.Success(emptyList())) }
                return@launch
            }

            _uiState.update { it.copy(results = UiState.Loading) }

            runCatching { searchUseCase.invoke(query = query, page = 1)?.movies.orEmpty() }
                .onSuccess { movies -> _uiState.update { it.copy(results = UiState.Success(movies)) } }
                .onFailure { e -> _uiState.update { it.copy(results = UiState.Error(e.message ?: "Unexpected error")) } }
        }
    }
}
