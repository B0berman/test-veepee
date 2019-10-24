package com.vp.detail.model

import com.google.gson.annotations.SerializedName
import com.vp.storage.MovieDetailDB

data class MovieDetail(@SerializedName("Title") val title: String,
                       @SerializedName("Year") val year: String,
                       @SerializedName("Runtime") val runtime: String,
                       @SerializedName("Director") val director: String,
                       @SerializedName("Plot") val plot: String,
                       @SerializedName("Poster") val poster: String,
                       @SerializedName("imdbID") val imdbID: String)

fun MovieDetail.toMovieDBEntity() =
        this.let {
            MovieDetailDB(
                    title = it.title,
                    year = it.year,
                    runtime = it.runtime,
                    director = it.director,
                    plot = it.plot,
                    poster = it.poster,
                    imdbID = it.imdbID
            )
        }