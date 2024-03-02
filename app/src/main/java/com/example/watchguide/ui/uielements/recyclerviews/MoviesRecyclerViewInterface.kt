package com.example.watchguide.ui.uielements.recyclerviews


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