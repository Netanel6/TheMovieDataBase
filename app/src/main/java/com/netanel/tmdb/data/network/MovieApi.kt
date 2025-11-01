package com.netanel.tmdb.data.network

import com.netanel.tmdb.domain.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
interface MovieApi {

    @GET
    suspend fun getUpcomingMovies(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse?>

    @GET
    suspend fun getNowPlayingMovies(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse?>

    @GET
    suspend fun getTopRatedMovies(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse?>

    @GET
    suspend fun getPopularMovies(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse?>


}