package com.netanel.tmdb.home

import com.netanel.tmdb.domain.movie.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Singleton
    @Provides
    fun provideNowPlayingUseCase(movieRepository: MovieRepository) : GetNowPlayingUseCase {
        return GetNowPlayingUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(movieApi: MovieApi) : MovieRepository {
        return MovieRepositoryImpl(movieApi)
    }
}