package com.example.watchguide.data

import com.example.watchguide.Movie
import com.example.watchguide.MoviePoster
import retrofit2.Call

interface MoviesRepositoryInterface {

    fun getMoviesFromRetrofit(): Call<List<Movie>?>

    fun getMoviePoster(): List<MoviePoster>
}