package com.netanel.tmdb.features.home


import com.netanel.tmdb.data.network.MovieApi
import com.netanel.tmdb.data.network.MovieDetailsApi
import com.netanel.tmdb.data.network.SearchApi
import com.netanel.tmdb.domain.repository.MovieRepository
import com.netanel.tmdb.domain.repository.MovieRepositoryImpl
import com.netanel.tmdb.domain.repository.SearchRepository
import com.netanel.tmdb.domain.repository.SearchRepositoryImpl
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
object SearchModule {

    @Singleton
    @Provides
    fun provideSearchRepository(searchApi: SearchApi) : SearchRepository {
        return SearchRepositoryImpl(searchApi)
    }
}