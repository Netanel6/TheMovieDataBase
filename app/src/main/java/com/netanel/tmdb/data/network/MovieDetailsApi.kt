package com.netanel.tmdb.data.network


import com.netanel.tmdb.domain.models.MovieDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MovieDetailsApi {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Url fullUrl: String,
        @Query("language") language: String = "en-US"
    ): Response<MovieDetailsResponse>
}
