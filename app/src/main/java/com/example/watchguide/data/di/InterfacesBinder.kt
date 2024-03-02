package com.example.watchguide.data.di

import com.example.watchguide.data.repository.MoviesRepositoryImpl
import com.example.watchguide.data.repository.MoviesRepositoryInterface
import com.example.watchguide.ui.uielements.fragments.MoviesListFragment
import com.example.watchguide.ui.uielements.recyclerviews.MoviesRecyclerViewInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfacesBinder {

    @Binds
    abstract fun bindMoviesRepositoryInterface(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepositoryInterface

    @Binds
    abstract fun bindMoviesRecyclerViewInterface(
        moviesListFragment: MoviesListFragment
    ): MoviesRecyclerViewInterface
}