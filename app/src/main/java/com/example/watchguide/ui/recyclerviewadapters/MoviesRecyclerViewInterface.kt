package com.example.watchguide.ui.recyclerviewadapters

import com.example.watchguide.Movie
import com.example.watchguide.MoviePoster

interface MoviesRecyclerViewInterface {

    fun onMovieClick(
        position: Int,
        moviesListWithoutPosters: List<Movie>?,
        moviesPostersList: List<MoviePoster>?
        )
}