package com.example.watchguide.ui.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watchguide.data.MoviesRepositoryImpl
import java.lang.IllegalArgumentException

class MoviesViewModelFactory(private val moviesRepositoryInterface: MoviesRepositoryImpl): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(moviesRepositoryInterface) as T
        }
        throw IllegalArgumentException("The view model doesn't find the repository dependency")
    }
}