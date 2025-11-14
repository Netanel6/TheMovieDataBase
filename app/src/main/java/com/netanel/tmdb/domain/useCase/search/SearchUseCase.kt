package com.netanel.tmdb.domain.useCase.search

import com.netanel.tmdb.domain.models.MovieResponse
import com.netanel.tmdb.domain.repository.SearchRepository
import javax.inject.Inject


/**
 * Created by netanelamar on 12/11/2025.
 * NetanelCA2@gmail.com
 */
class SearchUseCase @Inject constructor(val repository: SearchRepository) {

    suspend operator fun invoke(query: String): MovieResponse? {
        val movies = repository.searchMovieByQuery(query)
        return movies
    }
}