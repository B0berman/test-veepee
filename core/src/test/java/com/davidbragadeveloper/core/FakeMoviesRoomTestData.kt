package com.davidbragadeveloper.core

import com.davidbragadeveloper.core.entity.MovieRoomEntity

object FakeMoviesRoomTestData {
    val fakeRoomMovies = (1..9).map{
        MovieRoomEntity(
                id = 1,
                title = "title$it",
                year = "year$it",
                runtime = "runtime$it",
                director = "director$it",
                plot = "plot$it",
                favorite = it%2==0,
                poster = "poster$it"
        )
    }
}