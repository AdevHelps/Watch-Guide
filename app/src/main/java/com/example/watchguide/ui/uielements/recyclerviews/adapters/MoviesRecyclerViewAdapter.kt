package com.example.watchguide.ui.uielements.recyclerviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.watchguide.R
import com.example.watchguide.data.models.Movie
import com.example.watchguide.data.models.MoviePoster
import com.example.watchguide.databinding.WatchCardDesignBinding
import com.example.watchguide.ui.uielements.recyclerviews.MoviesRecyclerViewInterface

class MoviesRecyclerViewAdapter(
    private val moviesListWithoutPosters: List<Movie>?,
    private val moviesPostersList: List<MoviePoster>?,
    private val moviesRecyclerViewInterface: MoviesRecyclerViewInterface,
): RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            WatchCardDesignBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = moviesListWithoutPosters?.size ?: 0

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.binding.apply {
            if (moviesListWithoutPosters != null && moviesPostersList != null) {
                val movie = moviesListWithoutPosters[position]
                val moviePoster = moviesPostersList[position]

                movieCardCD.contentDescription = "${movie.title}, click for more"

                if (moviePoster.name == movie.title) {
                    moviePosterImageView.load(moviePoster.url)
                    moviePosterImageView.contentDescription = moviePoster.name
                } else {
                    moviePosterImageView.contentDescription = "No poster available"
                }

                movieTitleTV.text = movie.title

                when (movie.rate) {
                    in 0.1f..6.0f -> {
                        movieTrendingImageView.setImageResource(R.drawable.low_rate_icon)
                        rateTextView.text = movie.rate.toString()
                    }

                    in 6.1f..10.0f -> {
                        movieTrendingImageView.setImageResource(R.drawable.high_rate_icon)
                        rateTextView.text = movie.rate.toString()

                    }
                }
                movieTrendingImageView.contentDescription = movie.rate.toString()

                movieLanguageTV.text = movie.language
            }
        }
    }

    inner class MoviesViewHolder(
        val binding: WatchCardDesignBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.movieCardCD.setOnClickListener {
                val movieWithoutPoster = moviesListWithoutPosters?.get(adapterPosition)
                val moviePoster = moviesPostersList?.get(adapterPosition)

                if (moviePoster != null) {
                    if (movieWithoutPoster != null) {
                        moviesRecyclerViewInterface.onMovieClick(
                            adapterPosition,
                            moviePoster.url,
                            movieWithoutPoster.title,
                            movieWithoutPoster.genre,
                            movieWithoutPoster.plot,
                            movieWithoutPoster.actors
                        )
                    }
                }
            }
        }
    }
}