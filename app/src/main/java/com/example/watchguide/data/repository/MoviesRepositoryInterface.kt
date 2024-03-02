package com.example.watchguide.data.repository

import com.example.watchguide.data.models.Movie
import com.example.watchguide.data.models.MoviePoster
import retrofit2.Call

interface MoviesRepositoryInterface {

    fun getMoviesFromRetrofit(): Call<List<Movie>?>

    fun getMoviePoster(): List<MoviePoster>
}