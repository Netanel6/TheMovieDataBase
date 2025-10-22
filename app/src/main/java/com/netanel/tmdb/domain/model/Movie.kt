package com.netanel.tmdb.domain.model

import com.netanel.tmdb.utils.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate


/**
 * Created by netanelamar on 22/10/2025.
 * NetanelCA2@gmail.com
 */
@Serializable
data class MovieResponse(
    val results: List<Movie>,
    @SerialName("dates")
    val date: MovieDates,
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class MovieDates(
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("maximum")
    val maximumDate: LocalDate,

    @Serializable(with = LocalDateSerializer::class)
    @SerialName("minimum")
    val minimumDate: LocalDate
)


@Serializable
data class Movie(
    @SerialName("adult")
    val isAdult: Boolean,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("genre_ids")
    val genreIds: List<Int>,

    @SerialName("id")
    val id: Int,

    @SerialName("original_language")
    val originalLanguage: String,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("overview")
    val overview: String,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("release_date")
    val releaseDate: String,

    @SerialName("title")
    val title: String,

    @SerialName("video")
    val isVideo: Boolean,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int
)
