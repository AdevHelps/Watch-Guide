package com.example.watchguide.ui.stateholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.watchguide.data.datasources.MoviePoster
import com.example.watchguide.data.MoviesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MoviesViewModel(private val moviesRepositoryInterface: MoviesRepositoryImpl) : ViewModel() {

    var moviesPostersListLiveData = MutableLiveData<List<MoviePoster>>()
    var leftOffPositionLiveData = MutableLiveData<Int>()


    fun getMoviesPostersFromRepository() {
        runBlocking {
            val moviesPostures = withContext(Dispatchers.IO) {
                moviesRepositoryInterface.getMoviePoster()
            }
            moviesPostersListLiveData.value = moviesPostures
        }
    }
}