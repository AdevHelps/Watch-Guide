package com.example.watchguide.ui.stateholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.watchguide.data.Movie
import com.example.watchguide.data.datasources.MoviePoster
import com.example.watchguide.data.MoviesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel(private val moviesRepositoryInterface: MoviesRepositoryImpl) : ViewModel() {

    val onResponseLiveData = MutableLiveData<Pair<Call<List<Movie>?>, Response<List<Movie>?>>>()
    val onFailureLiveData = MutableLiveData<Pair<Call<List<Movie>?>, Throwable>>()
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

    fun sendRequest(){
        runBlocking {
            val moviesListCall = withContext(Dispatchers.IO) {
                moviesRepositoryInterface.getMoviesFromRetrofit()
            }

            val clonedMoviesListCall = moviesListCall.clone()
            clonedMoviesListCall.enqueue(object : Callback<List<Movie>?> {
                override fun onResponse(call: Call<List<Movie>?>, response: Response<List<Movie>?>) {
                    onResponseLiveData.value = Pair(call, response)
                }

                override fun onFailure(call: Call<List<Movie>?>, t: Throwable) {
                    onFailureLiveData.value = Pair(call, t)
                }
            })
        }
    }
}