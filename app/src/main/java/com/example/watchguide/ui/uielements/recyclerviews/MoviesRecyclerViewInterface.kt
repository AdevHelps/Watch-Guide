package com.example.watchguide.ui.uielements.recyclerviewadapters

import com.example.watchguide.data.datasources.MoviePoster

interface MoviesRecyclerViewInterface {

    fun onMovieClick(
        position: Int,
        moviePosterUrl: String,
        movieTitle: String,
        movieGenre: List<String>,
        moviePlot: String,
        movieActors: List<String>
    )
}