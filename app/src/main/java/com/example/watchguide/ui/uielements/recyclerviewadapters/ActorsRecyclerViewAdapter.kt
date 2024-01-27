package com.example.watchguide.ui.uielements.recyclerviewadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchguide.databinding.ActorCardDesignBinding

class ActorsRecyclerViewAdapter(
    private val actorsList: List<String>?
): RecyclerView.Adapter<ActorsRecyclerViewAdapter.ActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(ActorCardDesignBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.binding.apply {
            if (actorsList != null) {
                val actor = actorsList[position]
                actorCardView.contentDescription = actor
                actorNameTextView.text = actor
            }
        }
    }

    override fun getItemCount(): Int {
        return actorsList?.size ?: 0
    }

    class ActorViewHolder(val binding: ActorCardDesignBinding): RecyclerView.ViewHolder(binding.root)
}