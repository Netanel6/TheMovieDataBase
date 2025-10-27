package com.netanel.tmdb.useCase.movie

import com.netanel.tmdb.domain.movie.model.MovieResponse
import com.netanel.tmdb.home.MovieRepository
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
class GetTopRatedUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): MovieResponse? {
        val movies = repository.getTopRatedMovies()
        return movies
    }
}