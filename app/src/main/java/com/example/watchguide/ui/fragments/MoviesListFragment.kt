package com.example.watchguide.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchguide.Movie
import com.example.watchguide.MovieDetailsParcel
import com.example.watchguide.data.MoviesRepositoryImpl
import com.example.watchguide.domain.MoviesViewModel
import com.example.watchguide.R
import com.example.watchguide.databinding.FragmentMoviesListBinding
import com.example.watchguide.MoviePoster
import com.example.watchguide.ui.recyclerviewadapters.MoviesRecyclerViewAdapter
import com.example.watchguide.ui.recyclerviewadapters.MoviesRecyclerViewInterface
import com.example.watchguide.domain.MoviesViewModelFactory
import java.lang.Exception

class MoviesListFragment : Fragment(R.layout.fragment_movies_list), MoviesRecyclerViewInterface {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesListBinding.bind(view)
        binding.apply {

            refreshPageComponent.setColorSchemeResources(R.color.paleGreen)
            refreshPageComponent.setOnRefreshListener {
                if (connectionAvailable()) {

                    observeMoviesData(false)
                    refreshPageComponent.isRefreshing = false
                } else {

                    setUpMoviesRecyclerViewAdapter(null, null)

                    showExceptionInformer(true, R.drawable.no_wifi, "No internet connection")
                    refreshPageComponent.isRefreshing = false
                }
            }

            val moviesRepositoryInterface = MoviesRepositoryImpl()
            val moviesViewModelFactory = MoviesViewModelFactory(moviesRepositoryInterface)
            moviesViewModel = ViewModelProvider(
                this@MoviesListFragment,
                moviesViewModelFactory
            )[MoviesViewModel::class.java]

            if (connectionAvailable()) observeMoviesData(true)
            else {
                showExceptionInformer(true, R.drawable.no_wifi, "No internet connection")

            }
        }
    }

    private fun observeMoviesData(progressBarNeeded: Boolean) {
        binding.apply {

            if (progressBarNeeded) {
                moviesListProgressBar.visibility = View.VISIBLE
            }

            moviesViewModel.sendRequest()
            moviesViewModel.getMoviesPostersFromRepository()

            moviesViewModel.onResponseLiveData.observe(viewLifecycleOwner) { pair ->
                moviesViewModel.moviesPostersListLiveData
                    .observe(viewLifecycleOwner) { moviesPostersList ->
                    val response = pair.second

                    if (progressBarNeeded) {
                        moviesListProgressBar.visibility = View.GONE
                    }

                    if (response.isSuccessful) {

                        setUpMoviesRecyclerViewAdapter(
                            response.body(),
                            moviesPostersList
                        )
                        showExceptionInformer(false, null, null)
                    }

                        when(response.code()){
                            404 -> showExceptionInformer(
                                true,
                                R.drawable.server,
                                "Not found"
                            )
                            in 500..599 -> showExceptionInformer(
                                true,
                                R.drawable.server,
                                "Server side error"
                            )
                        }

                    moviesViewModel.onFailureLiveData.observe(viewLifecycleOwner) { pair ->
                        val throwable = pair.second

                        if (progressBarNeeded) {
                            moviesListProgressBar.visibility = View.GONE
                        }

                        if (throwable is Exception) {
                            showExceptionInformer(true, R.drawable.server, "Unexpected error")
                        }
                    }
                }
            }
        }
    }

    private fun setUpMoviesRecyclerViewAdapter(
        moviesListWithoutPosters: List<Movie>?,
        moviesPostersList: List<MoviePoster>?,
    ) {
        binding.apply {

            val adapter = MoviesRecyclerViewAdapter(
                moviesListWithoutPosters,
                moviesPostersList,
                this@MoviesListFragment
            )
            moviesRVLayout.adapter = adapter
            moviesRVLayout.layoutManager = LinearLayoutManager(requireContext())

            moviesViewModel.leftOffPositionLiveData.observe(viewLifecycleOwner) { leftOffPosition ->
                moviesRVLayout.scrollToPosition(leftOffPosition)
            }
        }
    }

    private fun showExceptionInformer(show: Boolean, nullableImage: Int?, text: String?) {
        binding.apply {
            if (show) {
                exceptionInformerLinearLayout.visibility = View.VISIBLE
                nullableImage.let {
                    val image = nullableImage!!
                    exceptionInformerImageView.setImageResource(image)
                }
                exceptionInformerTextView.text = text
                exceptionInformerImageView.contentDescription = text

            } else {
                exceptionInformerLinearLayout.visibility = View.GONE
            }
        }
    }

    private fun connectionAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    override fun onMovieClick(
        position: Int,
        moviesListWithoutPosters: List<Movie>?,
        moviesPostersList: List<MoviePoster>?,
    ) {

        moviesViewModel.leftOffPositionLiveData.value = position

        moviesViewModel.onResponseLiveData.observe(viewLifecycleOwner) { pair ->
            moviesViewModel.moviesPostersListLiveData.observe(viewLifecycleOwner) { moviePosterList ->

                val response = pair.second

                val moviesList = response.body()
                if (moviesList != null && moviePosterList != null) {

                    val movie = moviesList[position]
                    val movieDetailsParceled = MovieDetailsParcel(
                        position,
                        moviePosterList[position].url,
                        movie.title,
                        movie.genre,
                        movie.plot,
                        movie.actors
                    )

                    val action =
                        MoviesListFragmentDirections
                            .actionMoviesListFragmentToMovieDetailsFragment(
                                movieDetailsParceled
                            )
                    findNavController().navigate(action)
                }
            }
        }
    }
}