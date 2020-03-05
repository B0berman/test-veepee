package com.vp.api.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = ListItem.TABLE_NAME)
data class ListItem(
    @PrimaryKey @Keep val imdbID: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Poster") val poster: String?
) {

    companion object {
        const val TABLE_NAME = "favorite"
    }
}