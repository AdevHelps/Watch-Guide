package com.example.watchguide.ui.stateholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.models.movie.MoviePoster
import com.example.watchguide.data.repository.MoviesRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepositoryInterface: MoviesRepositoryInterface,
    private val moviesPostersSource: MoviesPostersSource
) : ViewModel() {

    var leftOffPositionLiveData = MutableLiveData<Int>()

    fun getMoviesPostersFromRepository(): MutableLiveData<List<MoviePoster>> {
        val moviesPostersListLiveData = MutableLiveData<List<MoviePoster>>()
        val moviesPostures = runBlocking {
            withContext(Dispatchers.IO) {
                moviesRepositoryInterface.getMoviePoster(moviesPostersSource)
            }
        }
        moviesPostersListLiveData.value = moviesPostures
        return moviesPostersListLiveData
    }
}