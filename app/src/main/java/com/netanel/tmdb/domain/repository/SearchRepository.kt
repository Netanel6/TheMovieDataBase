package com.netanel.tmdb.domain.repository

import com.netanel.tmdb.data.network.SearchApi
import com.netanel.tmdb.domain.Constants
import com.netanel.tmdb.domain.models.MovieResponse
import javax.inject.Inject


/**
 * Created by netanelamar on 12/11/2025.
 * NetanelCA2@gmail.com
 */

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun searchMovieByQuery(query: String): MovieResponse? {
        val response = searchApi.searchMovieByQuery(
            fullUrl = "${Constants.MOVIES_URL}/search/movie?query=$query"
        )
        return if (response.isSuccessful) response.body() else null
    }

}

interface SearchRepository {
    suspend fun searchMovieByQuery(query: String): MovieResponse?
}