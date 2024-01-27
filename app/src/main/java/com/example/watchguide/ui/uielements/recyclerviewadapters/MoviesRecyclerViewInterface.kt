package com.example.watchguide.ui.recyclerviewadapters

import com.example.watchguide.data.Movie
import com.example.watchguide.data.datasources.MoviePoster

interface MoviesRecyclerViewInterface {

    fun onMovieClick(
        position: Int,
        moviesListWithoutPosters: List<Movie>?,
        moviesPostersList: List<MoviePoster>?
        )
}