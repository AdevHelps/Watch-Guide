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
import com.example.watchguide.ui.stateholder.MoviesViewModel
import com.example.watchguide.R
import com.example.watchguide.databinding.FragmentMoviesListBinding
import com.example.watchguide.data.models.ExceptionTypes
import com.example.watchguide.data.models.Movie
import com.example.watchguide.data.models.MovieDetailsParcelModel
import com.example.watchguide.data.models.MoviePoster
import com.example.watchguide.data.models.NetworkStates
import com.example.watchguide.data.repository.MoviesRepositoryInterface
import com.example.watchguide.ui.stateholder.ConnectionLiveData
import com.example.watchguide.ui.stateholder.MoviesViewModelFactory
import com.example.watchguide.ui.uielements.recyclerviews.MoviesRecyclerViewInterface
import com.example.watchguide.ui.uielements.recyclerviews.adapters.MoviesRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list), MoviesRecyclerViewInterface {

    private lateinit var binding: FragmentMoviesListBinding
    @Inject lateinit var moviesRepositoryInterface: MoviesRepositoryInterface
    private lateinit var moviesViewModel: MoviesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesListBinding.bind(view)
        binding.apply {

            val moviesViewModelFactory = MoviesViewModelFactory(moviesRepositoryInterface)
            moviesViewModel = ViewModelProvider(
                this@MoviesListFragment,
                moviesViewModelFactory
            )[MoviesViewModel::class.java]

            val c = ConnectionLiveData(requireContext())
            c.observe(viewLifecycleOwner) { isNetworkAvailable ->

                if (isNetworkAvailable == NetworkStates.OnAvailable.name) {
                    showExceptionInformer(ExceptionTypes.NullException)
                    observeMoviesData(true)
                }
            }

            refreshPageComponent.setColorSchemeResources(R.color.paleGreen)
            refreshPageComponent.setOnRefreshListener {
                if (connectionAvailable()) {

                    observeMoviesData(false)
                    refreshPageComponent.isRefreshing = false
                } else {

                    refreshPageComponent.isRefreshing = false
                }
            }

            if (connectionAvailable()) observeMoviesData(true)
            else {
                showExceptionInformer(ExceptionTypes.NoInternetConnection)
            }
        }
    }

    private fun observeMoviesData(progressBarNeeded: Boolean) {
        binding.apply {

            if (progressBarNeeded) {
                moviesListProgressBar.visibility = View.VISIBLE
            }

            moviesViewModel.getMoviesPostersFromRepository()
            CoroutineScope(Dispatchers.IO).launch {
                val moviesListCall =async {
                    moviesRepositoryInterface.getMoviesFromRetrofit()
                }.await()

                async {
                    val clonedMoviesListCall = moviesListCall.clone()
                    clonedMoviesListCall.enqueue(object : Callback<List<Movie>?> {
                        override fun onResponse(
                            call: Call<List<Movie>?>,
                            response: Response<List<Movie>?>
                        ) {

                            if (progressBarNeeded) {
                                moviesListProgressBar.visibility = View.GONE
                            }

                            moviesViewModel.getMoviesPostersFromRepository()
                                .observe(viewLifecycleOwner) { moviesPostersList ->

                                    if (response.isSuccessful) {

                                        setUpMoviesRecyclerViewAdapter(
                                            response.body(),
                                            moviesPostersList
                                        )
                                        showExceptionInformer(ExceptionTypes.NullException)
                                    }

                                    when (response.code()) {
                                        404 -> showExceptionInformer(ExceptionTypes.NotFound)

                                        in 500..599 -> showExceptionInformer(
                                            ExceptionTypes.ServerSideError
                                        )
                                    }
                                }
                        }

                        override fun onFailure(call: Call<List<Movie>?>, t: Throwable) {

                            if (progressBarNeeded) {
                                moviesListProgressBar.visibility = View.GONE
                            }

                            if (t is Exception) {
                                showExceptionInformer(ExceptionTypes.UnexpectedError)
                            }
                        }
                    })
                }.await()
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

    private fun showExceptionInformer(exceptionTypes: ExceptionTypes) {
        binding.apply {
            if (exceptionTypes == ExceptionTypes.NullException) {
                exceptionInformerLinearLayout.visibility = View.GONE

            }else {
                exceptionInformerLinearLayout.visibility = View.VISIBLE
                if (exceptionTypes.image != null) {
                    exceptionInformerImageView.setImageResource(exceptionTypes.image)
                }
                exceptionInformerTextView.text = exceptionTypes.message
                exceptionInformerImageView.contentDescription = exceptionTypes.message
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