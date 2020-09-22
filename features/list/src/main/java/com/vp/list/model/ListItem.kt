package com.vp.list.model

import com.google.gson.annotations.SerializedName

class ListItem(
  @SerializedName("Title") val title: String,
  @SerializedName("Year") val year: String,
  val imdbID: String,
  @SerializedName("Poster") val poster: String
)