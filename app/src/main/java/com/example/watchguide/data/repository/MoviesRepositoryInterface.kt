package com.example.watchguide.data.repository

import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.datasources.MoviesWebServiceAPIsInterface
import com.example.watchguide.data.models.movie.Movie
import com.example.watchguide.data.models.movie.MoviePoster
import retrofit2.Call

interface MoviesRepositoryInterface {

    fun getMoviesFromRetrofit(
        moviesWebServiceAPIsInterface: MoviesWebServiceAPIsInterface
    ): Call<List<Movie>?>

    fun getMoviePoster(moviesPostersSource: MoviesPostersSource): List<MoviePoster>
}