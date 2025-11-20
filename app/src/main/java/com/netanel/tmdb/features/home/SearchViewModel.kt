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
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by netanelamar on 12/11/2025.
 * NetanelCA2@gmail.com
 */
@HiltViewModel
class SearchViewModel @Inject constructor(val searchUseCase: SearchUseCase) : ViewModel() {

    private val _searchUiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val searchUiState: StateFlow<UiState<List<Movie>>> = _searchUiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    val query: StateFlow<String> = searchQuery.asStateFlow()


    fun onQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
        if (newQuery.isBlank()) {
            _searchUiState.value = UiState.Success(emptyList())
        }
    }

    fun searchMovies() {
        viewModelScope.launch {
            val currentQuery = searchQuery.value.trim()
            if (currentQuery.isEmpty()) {
                _searchUiState.value = UiState.Success(emptyList())
                return@launch
            }

            _searchUiState.value = UiState.Loading

            try {
                val response = searchUseCase.invoke(query = currentQuery)
                _searchUiState.value = UiState.Success(
                    data = response?.movies ?: emptyList()
                )
            } catch (e: Exception) {
                _searchUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
    }
}