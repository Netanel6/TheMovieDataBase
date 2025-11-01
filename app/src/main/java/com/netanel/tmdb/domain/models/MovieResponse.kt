package com.netanel.tmdb.domain.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


/**
 * Created by netanelamar on 22/10/2025.
 * NetanelCA2@gmail.com
 */
@Serializable
data class MovieResponse(
    val results: List<Movie>,
    @SerializedName("dates")
    val date: MovieDates,
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

@Serializable
data class MovieDates(
    @SerializedName("maximum")
    val maximumDate: String,

    @SerializedName("minimum")
    val minimumDate: String
)


@Serializable
data class Movie(
    @SerializedName("adult")
    val isAdult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("id")
    val id: Int,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val isVideo: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
)
