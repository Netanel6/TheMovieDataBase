package com.netanel.tmdb.home

import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.movie.MovieApi
import com.netanel.tmdb.domain.movie.model.MovieResponse
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
): MovieRepository {
    override suspend fun getNowPlayingMovies(): MovieResponse? {
        val response = movieApi.getNowPlayingMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/now_playing"
        )
        return if (response.isSuccessful) response.body() else null
    }
}
interface MovieRepository {
    suspend fun getNowPlayingMovies(): MovieResponse?
}