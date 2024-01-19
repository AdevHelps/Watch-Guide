package com.example.watchguide

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val year: Short,
    @SerializedName("rating")
    val rate: Float,
    val genre: List<String>,
    val plot: String,
    val actors: List<String>,
    val language: String
)