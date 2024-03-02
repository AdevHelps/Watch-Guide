package com.example.watchguide.ui.uielements.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.watchguide.R
import com.example.watchguide.databinding.FragmentMovieDetailsBinding
import com.example.watchguide.ui.uielements.recyclerviews.adapters.ActorsRecyclerViewAdapter
import com.example.watchguide.ui.uielements.recyclerviews.adapters.MovieGenreRecyclerViewAdapter

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val args by navArgs<MovieDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)
        binding.apply {

            val passedMovieDetails = args.movieDetailsArgsName

            backImageButton.setOnClickListener {
                findNavController().popBackStack()
            }

            moviePosterImageDetail.load(passedMovieDetails.posterUrl)
            moviePosterImageDetail.contentDescription = "${passedMovieDetails.title} + poster"

            movieTitleTV.text = passedMovieDetails.title

            val genderList = passedMovieDetails.genre
            val movieGenreRecyclerViewAdapter = MovieGenreRecyclerViewAdapter(genderList)
            setUpRecyclerView(movieGenreRecyclerViewAdapter, genreRecyclerView)

            moviePlotTextView.text = passedMovieDetails.plot

            val actorsList = passedMovieDetails.actors
            val actorsRecyclerViewAdapter = ActorsRecyclerViewAdapter(actorsList)
            setUpRecyclerView(actorsRecyclerViewAdapter, actorsRecyclerView)


            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().popBackStack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
    }

    private fun setUpRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView){
        recyclerView.adapter = adapter
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }
}