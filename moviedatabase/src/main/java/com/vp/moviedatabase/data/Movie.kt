package com.vp.moviedatabase.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id:String,
        val title:String,
        val plot:String,
        val director:String,
        val runtime:String,
        val year:String,
        @ColumnInfo(name = "poster_id")
        val posterId:String)