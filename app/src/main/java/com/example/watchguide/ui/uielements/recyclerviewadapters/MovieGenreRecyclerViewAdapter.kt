package com.example.watchguide.ui.uielements.recyclerviewadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchguide.databinding.GenderButtonDesignBinding

class MovieGenreRecyclerViewAdapter(
    private val genreList: List<String>?
): RecyclerView.Adapter<MovieGenreRecyclerViewAdapter.GenderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        return GenderViewHolder(GenderButtonDesignBinding.inflate(LayoutInflater.from(
            parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return genreList?.size ?: 0
    }

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.binding.apply {
            if(genreList != null) {
                val genre = genreList[position]
                genderButton.text = genre
            }
        }
    }

    class GenderViewHolder(val binding: GenderButtonDesignBinding): RecyclerView.ViewHolder(binding.root)
}