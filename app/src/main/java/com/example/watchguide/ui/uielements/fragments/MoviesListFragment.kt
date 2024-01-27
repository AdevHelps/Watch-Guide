package com.example.watchguide.ui.uielements.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchguide.data.Movie
import com.example.watchguide.ui.MovieDetailsParcelModel
import com.example.watchguide.data.MoviesRepositoryImpl
import com.example.watchguide.ui.stateholder.MoviesViewModel
import com.example.watchguide.R
import com.example.watchguide.databinding.FragmentMoviesListBinding
import com.example.watchguide.data.datasources.MoviePoster
import com.example.watchguide.ui.uielements.recyclerviewadapters.MoviesRecyclerViewAdapter
import com.example.watchguide.ui.uielements.recyclerviewadapters.MoviesRecyclerViewInterface
import com.example.watchguide.ui.stateholder.MoviesViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MoviesListFragment : Fragment(R.layout.fragment_movies_list), MoviesRecyclerViewInterface {

    private lateinit var binding: FragmentMoviesListBinding
    private val moviesRepositoryInterface = MoviesRepositoryImpl()
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

            moviesViewModel.getMoviesPostersFromRepository()
            runBlocking {
                val moviesListCall = withContext(Dispatchers.IO) {
                    moviesRepositoryInterface.getMoviesFromRetrofit()
                }

                val clonedMoviesListCall = moviesListCall.clone()
                clonedMoviesListCall.enqueue(object : Callback<List<Movie>?> {
                    override fun onResponse(call: Call<List<Movie>?>, response: Response<List<Movie>?>) {

                        if (progressBarNeeded) {
                            moviesListProgressBar.visibility = View.GONE
                        }

                        moviesViewModel.moviesPostersListLiveData
                            .observe(viewLifecycleOwner) { moviesPostersList ->

                                if (response.isSuccessful) {

                                    setUpMoviesRecyclerViewAdapter(
                                        response.body(),
                                        moviesPostersList
                                    )
                                    showExceptionInformer(false, null, null)
                                }

                                when (response.code()) {
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
                            }
                    }

                    override fun onFailure(call: Call<List<Movie>?>, t: Throwable) {

                        if (progressBarNeeded) {
                            moviesListProgressBar.visibility = View.GONE
                        }

                        if (t is Exception) {
                            showExceptionInformer(true, R.drawable.server, "Unexpected error")
                        }
                    }
                })
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
        moviePosterUrl: String,
        movieTitle: String,
        movieGenre: List<String>,
        moviePlot: String,
        movieActors: List<String>
    ) {

        moviesViewModel.leftOffPositionLiveData.value = position

        val movieDetailsParceled = MovieDetailsParcelModel(
            moviePosterUrl,
            movieTitle,
            movieGenre,
            moviePlot,
            movieActors
        )

        val action =
            MoviesListFragmentDirections
                .actionMoviesListFragmentToMovieDetailsFragment(
                    movieDetailsParceled
                )
        findNavController().navigate(action)
    }
}