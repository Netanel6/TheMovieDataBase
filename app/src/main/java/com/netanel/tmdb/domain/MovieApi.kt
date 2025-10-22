package com.netanel.tmdb.domain

import com.netanel.tmdb.domain.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
interface MovieApi {

    @GET("movie/now_playing")
    suspend fun getMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse
}