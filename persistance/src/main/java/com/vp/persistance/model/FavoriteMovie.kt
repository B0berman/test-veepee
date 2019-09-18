package com.vp.persistance.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Uxio Lorenzo on 2019-09-09.
 */
@Entity
data class FavoriteMovie(
    @PrimaryKey
    val omdbId: String,
    val title: String,
    val year: String,
    val poster: String,
    val plot: String,
    val director: String,
    val runtime: String)