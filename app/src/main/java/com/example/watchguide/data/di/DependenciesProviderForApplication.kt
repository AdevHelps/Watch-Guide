package com.example.watchguide.data.di

import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.datasources.MoviesWebServiceAPIsInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DependenciesProviderForApplication {

    @Provides
    fun provideMoviesWebServiceAPIsInterface(retrofit: Retrofit): MoviesWebServiceAPIsInterface {
        return retrofit.create(MoviesWebServiceAPIsInterface::class.java)
    }

    @Provides
    fun provideMoviesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://freetestapi.com")
            .build()
    }

    @Provides
    fun provideMoviesPostersSource(): MoviesPostersSource {
        return MoviesPostersSource()
    }
}