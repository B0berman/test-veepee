/*
 * Created by Alexis Rodriguez Paret on 3/13/20 8:57 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 8:42 PM
 *
 */

package com.vp.persistance.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_table")
public final class MovieModel(
        @PrimaryKey
        @SerializedName("imdbID") val imdbID: String,
        @SerializedName("Title") val title: String,
        @SerializedName("Year") val year: String,
        @SerializedName("Runtime") val runtime: String,
        @SerializedName("Director") val director: String,
        @SerializedName("Plot") val plot: String,
        @SerializedName("Poster") val poster: String)