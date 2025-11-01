package com.netanel.tmdb.features.home

import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.data.network.MovieApi
import com.netanel.tmdb.domain.models.MovieResponse
import com.netanel.tmdb.domain.models.MovieDetailsResponse
import com.netanel.tmdb.data.network.MovieDetailsApi
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDetailsApi: MovieDetailsApi
) : MovieRepository {

    override suspend fun getUpcomingMovies(): MovieResponse? {
        val response = movieApi.getUpcomingMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/upcoming"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getNowPlayingMovies(): MovieResponse? {
        val response = movieApi.getNowPlayingMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/now_playing"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getPopularMovies(): MovieResponse? {
        val response = movieApi.getPopularMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/popular"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getTopRatedMovies(): MovieResponse? {
        val response = movieApi.getTopRatedMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/top_rated"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse? {
        val response = movieDetailsApi.getMovieDetails(
            fullUrl = "${Constants.MOVIES_URL}movie/$movieId"
        )
        return  if (response.isSuccessful) response.body() else null
    }

}

interface MovieRepository {
    suspend fun getNowPlayingMovies(): MovieResponse?
    suspend fun getPopularMovies(): MovieResponse?
    suspend fun getTopRatedMovies(): MovieResponse?
    suspend fun getUpcomingMovies(): MovieResponse?
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse?
}