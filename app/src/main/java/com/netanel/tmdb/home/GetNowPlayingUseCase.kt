package com.netanel.tmdb.home

import com.netanel.tmdb.domain.movie.model.MovieResponse
import javax.inject.Inject


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
class GetNowPlayingUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): MovieResponse? {
        val movies = repository.getNowPlayingMovies()
        // כאן אפשר להוסיף לוגיקת ביזנס אם צריך
        return movies
    }
}