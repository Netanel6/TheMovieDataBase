package com.netanel.tmdb.features.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.domain.useCase.movieDetails.GetMovieDetailsUseCase
import com.netanel.tmdb.features.home.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by netanelamar on 29/10/2025.
 * NetanelCA2@gmail.com
 */

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val getMovieDetailsUseCase: GetMovieDetailsUseCase) :
    ViewModel() {

    private val _movieDetailsUiState: MutableStateFlow<UiState<MovieDetailsResponse?>> =
        MutableStateFlow(UiState.Loading)
    val movieDetailsUiState: StateFlow<UiState<MovieDetailsResponse?>> =
        _movieDetailsUiState.asStateFlow()

    fun getMovieDetails(movieId: Int): MutableStateFlow<UiState<MovieDetailsResponse?>> {
        viewModelScope.launch {
            try {
                val movies = getMovieDetailsUseCase.invoke(movieId)
                _movieDetailsUiState.value = UiState.Success(
                    data = movies
                )
            } catch (e: Exception) {
                _movieDetailsUiState.value = UiState.Error(e.message ?: "Unexpected error")
            }
        }
        return _movieDetailsUiState
    }


}