package com.davidbragadeveloper.core

import com.davidbragadeveloper.core.entity.MovieRoomEntity
import com.vp.detail.model.MovieDetail


object FakeMoviesRoomTestData {
    val fakeRoomMovies = (1..9).map{
        MovieRoomEntity(
                id = "$it",
                title = "title$it",
                year = "year$it",
                runtime = "runtime$it",
                director = "director$it",
                plot = "plot$it",
                favorite = it % 2 == 0,
                poster = "poster$it"
        )
    }

    val fakeMovies = (1..9).map{
        MovieDetail(
                imdbID = "$it",
                title = "title$it",
                year = "year$it",
                runtime = "runtime$it",
                director = "director$it",
                plot = "plot$it",
                favorite = it % 2 == 0,
                poster = "poster$it"
        )
    }
}