package com.netanel.tmdb.features.home

import com.netanel.tmdb.data.network.MovieApi
import com.netanel.tmdb.data.network.MovieDetailsApi
import com.netanel.tmdb.domain.useCase.movie.GetNowPlayingUseCase
import com.netanel.tmdb.domain.useCase.movie.GetPopularUseCase
import com.netanel.tmdb.domain.useCase.movie.GetTopRatedUseCase
import com.netanel.tmdb.domain.useCase.movie.GetUpcomingUseCase
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
object MovieModule {

    @Singleton
    @Provides
    fun provideUpcomingUseCase(movieRepository: MovieRepository) : GetUpcomingUseCase {
        return GetUpcomingUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideNowPlayingUseCase(movieRepository: MovieRepository) : GetNowPlayingUseCase {
        return GetNowPlayingUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideTopRatedUseCase(movieRepository: MovieRepository) : GetTopRatedUseCase {
        return GetTopRatedUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun providePopularUseCase(movieRepository: MovieRepository) : GetPopularUseCase {
        return GetPopularUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(movieApi: MovieApi, movieDetailsApi: MovieDetailsApi) : MovieRepository {
        return MovieRepositoryImpl(movieApi, movieDetailsApi)
    }
}