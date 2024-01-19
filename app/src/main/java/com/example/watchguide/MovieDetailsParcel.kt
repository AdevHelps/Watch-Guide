package com.example.watchguide

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsParcel(
    val position: Int,
    val posterUrl: String,
    val title: String?,
    val genre: List<String>?,
    val plot: String?,
    val actors: List<String>?
): Parcelable