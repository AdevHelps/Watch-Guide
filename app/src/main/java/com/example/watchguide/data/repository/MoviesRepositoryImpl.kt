package com.example.watchguide.data.repository

import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.datasources.MoviesWebServiceAPIsInterface
import com.example.watchguide.data.models.movie.Movie
import com.example.watchguide.data.models.movie.MoviePoster
import retrofit2.Call
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(): MoviesRepositoryInterface {

    override fun getMoviesFromRetrofit(
        moviesWebServiceAPIsInterface: MoviesWebServiceAPIsInterface
    ): Call<List<Movie>?> {
        return moviesWebServiceAPIsInterface.getMovies()
    }

    override fun getMoviePoster(moviesPostersSource: MoviesPostersSource): List<MoviePoster> {
        return moviesPostersSource.moviesPostersList
    }
}