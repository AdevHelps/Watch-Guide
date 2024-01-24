package com.example.watchguide.data.datasources.moviessource

import com.example.watchguide.data.Movie
import retrofit2.Call
import retrofit2.http.GET

interface MoviesWebServiceAPIsInterface {

    @GET("api/v1/movies")
    fun getMovies(): Call<List<Movie>?>

}