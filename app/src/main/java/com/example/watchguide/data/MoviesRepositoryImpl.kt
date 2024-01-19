package com.example.watchguide.data

import com.example.watchguide.Movie
import com.example.watchguide.MoviePoster
import com.example.watchguide.data.datasources.MoviesPostersSource
import com.example.watchguide.data.datasources.moviessource.Client
import retrofit2.Call

class MoviesRepositoryImpl: MoviesRepositoryInterface {

    private val moviesPostersSource = MoviesPostersSource()

    override fun getMoviesFromRetrofit(): Call<List<Movie>?> {
        return Client.moviesWebServiceAPIsInterface.getMovies()
    }

    override fun getMoviePoster(): List<MoviePoster> {
        return moviesPostersSource.moviesPostersList
    }
}