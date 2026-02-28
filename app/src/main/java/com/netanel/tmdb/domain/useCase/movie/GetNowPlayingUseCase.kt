package com.netanel.tmdb.domain.useCase.movie

import com.netanel.tmdb.domain.BaseResponse
import com.netanel.tmdb.domain.models.MovieResponse
import com.netanel.tmdb.domain.repository.MovieRepository
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
class GetNowPlayingUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int): MovieResponse? {
        return when(val result = repository.getNowPlayingMovies(page)){
            is BaseResponse.Error -> null
            is BaseResponse.Success -> result.data
        }
    }
}