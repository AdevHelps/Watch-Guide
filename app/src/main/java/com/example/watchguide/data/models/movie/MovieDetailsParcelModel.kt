package com.example.watchguide.data.models.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsParcelModel(
    val posterUrl: String,
    val title: String?,
    val genre: List<String>?,
    val plot: String?,
    val actors: List<String>?
): Parcelable