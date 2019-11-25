package com.vp.favorites.core.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

// ToDo : create a Movie model and move this one in data/room - CE

@Entity
data class Movie(@PrimaryKey
                 val movieId : String,
                 val title: String,
                 val year: String,
                 val runtime: String,
                 val director: String,
                 val plot: String,
                 val poster: String = "")