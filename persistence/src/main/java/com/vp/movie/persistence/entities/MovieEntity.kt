package com.vp.movie.persistence.entities

import androidx.room.Entity
import com.vp.movie.abstraction.dto.Movie

@Entity(tableName = "favourites", primaryKeys = ["imdbID"])
data class MovieEntity(
        override val title:String,
        override val year:String,
        override val imdbID:String,
        override val poster:String
):Movie