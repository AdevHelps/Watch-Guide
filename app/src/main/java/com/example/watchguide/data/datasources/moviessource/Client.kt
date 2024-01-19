package com.example.watchguide.data.datasources.moviessource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private const val baseURL = "https://freetestapi.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()

    val moviesWebServiceAPIsInterface: MoviesWebServiceAPIsInterface =
        retrofit.create(MoviesWebServiceAPIsInterface::class.java)
}