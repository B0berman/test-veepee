package com.vp.moviedatabase.data

import org.hamcrest.CoreMatchers.`is` as iz
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when` as WHEN
import org.mockito.Mockito.mock

class FavoriteMovieLogicTest {

    lateinit var logic: FavoriteMovieLogic
    val movieDao = mock(FavoriteMovieDao::class.java)

    @Before
    fun setup() {
    }

    @Test
    fun givenMovieIsNotFavoriteWhenAskingIfItIsThenReturnFalse() {
        //given
        val movieId = "1"
        WHEN(movieDao.getMovie(movieId)).thenReturn(null)
        logic = FavoriteMovieLogic(movieDao)
        assertThat(logic.isFavorite(movieId), iz(false))
    }
    @Test
    fun givenMovieIsFavoriteWhenAskingIfItIsThenReturnTrue() {
        //given
        val oneFavoriteMovie = Movie("1",
                "One",
                "One plot",
                "One Director",
                "One runtime",
                "Year One",
                "One Poster Id")
        WHEN(movieDao.getMovie(oneFavoriteMovie.id)).thenReturn(oneFavoriteMovie)
        logic = FavoriteMovieLogic(movieDao)
        assertThat(logic.isFavorite(oneFavoriteMovie.id), iz(true))
    }



}