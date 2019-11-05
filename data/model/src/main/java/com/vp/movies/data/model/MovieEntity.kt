package com.vp.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class MovieEntity(
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Year")
    val year: String? = null,
    @PrimaryKey
    val imdbID: String,
    @SerializedName("Poster")
    val poster: String? = null,
    @SerializedName("Runtime")
    val runtime: String? = null,
    @SerializedName("Director")
    val director: String? = null,
    @SerializedName("Plot")
    val plot: String? = null
)
