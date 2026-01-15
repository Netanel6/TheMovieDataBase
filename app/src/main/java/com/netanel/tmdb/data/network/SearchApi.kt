package com.netanel.tmdb.data.network

import com.netanel.tmdb.domain.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * Created by netanelamar on 12/11/2025.
 * NetanelCA2@gmail.com
 */
interface SearchApi {

    @GET
    suspend fun searchMovieByQuery(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US",
    ): Response<MovieResponse>
}