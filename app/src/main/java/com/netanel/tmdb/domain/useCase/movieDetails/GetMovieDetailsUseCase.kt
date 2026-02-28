package com.netanel.tmdb.domain.useCase.movieDetails

import com.netanel.tmdb.domain.BaseResponse
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.domain.repository.MovieRepository
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetailsResponse? {
        return when(val result = repository.getMovieDetails(movieId)){
            is BaseResponse.Error -> null
            is BaseResponse.Success -> result.data
        }
    }
}