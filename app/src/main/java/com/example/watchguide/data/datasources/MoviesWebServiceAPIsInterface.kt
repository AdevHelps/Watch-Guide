package com.example.watchguide.data.datasources

import com.example.watchguide.data.models.Movie
import retrofit2.Call
import retrofit2.http.GET

interface MoviesWebServiceAPIsInterface {

    @GET("api/v1/movies")
    fun getMovies(): Call<List<Movie>?>

}