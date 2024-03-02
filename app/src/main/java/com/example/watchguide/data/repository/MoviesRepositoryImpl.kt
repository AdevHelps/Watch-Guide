package com.example.watchguide.data.repository

import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.datasources.MoviesWebServiceAPIsInterface
import com.example.watchguide.data.models.Movie
import com.example.watchguide.data.models.MoviePoster
import retrofit2.Call
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesWebServiceAPIsInterface: MoviesWebServiceAPIsInterface,
    private val moviesPostersSource: MoviesPostersSource
): MoviesRepositoryInterface {


    override fun getMoviesFromRetrofit(): Call<List<Movie>?> {
        return moviesWebServiceAPIsInterface.getMovies()
    }

    override fun getMoviePoster(): List<MoviePoster> {
        return moviesPostersSource.moviesPostersList
    }
}