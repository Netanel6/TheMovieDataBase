package com.netanel.tmdb.domain.repository

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

    override suspend fun getUpcomingMovies(page: Int): MovieResponse? {
        val response = movieApi.getUpcomingMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/upcoming?page=$page"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getNowPlayingMovies(page: Int): MovieResponse? {
        val response = movieApi.getNowPlayingMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/now_playing?page=$page"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getPopularMovies(page: Int): MovieResponse? {
        val response = movieApi.getPopularMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/popular?page=$page"
        )
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getTopRatedMovies(page: Int): MovieResponse? {
        val response = movieApi.getTopRatedMovies(
            fullUrl = "${Constants.MOVIES_URL}movie/top_rated?page=$page"
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
    suspend fun getNowPlayingMovies(page: Int = 1): MovieResponse?
    suspend fun getPopularMovies(page: Int = 1): MovieResponse?
    suspend fun getTopRatedMovies(page: Int = 1): MovieResponse?
    suspend fun getUpcomingMovies(page: Int = 1): MovieResponse?
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse?
}