package com.davidbragadeveloper.core

import com.davidbragadeveloper.core.dao.MovieDao
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoviesRoomDataSourceImpTest{

    private val movieDao: MovieDao = mock()
    private lateinit var  dataSource : MoviesRoomDataSource

    @Before
    fun setup(){
        dataSource = MoviesRoomDataSourceImp(movieDao)
    }

    @Test
    fun `should getAllMovies return received list of movies`(){
        whenever(movieDao.getAll()).thenReturn(FakeMoviesRoomTestData.fakeRoomMovies)
        assertEquals(FakeMoviesRoomTestData.fakeRoomMovies, dataSource.getAllMovies())
    }

    @Test
    fun `should getFavoriteMovies return received list of movies`(){
        whenever(movieDao.getFavorites()).thenReturn(FakeMoviesRoomTestData.fakeRoomMovies)
        assertEquals(FakeMoviesRoomTestData.fakeRoomMovies, dataSource.getFavoriteMovies())
    }

    @Test
    fun `should insert return true when data is inserted`(){
        whenever(movieDao.insert(any())).thenReturn(listOf(1L))
        assertEquals(true, dataSource.insertMovies(FakeMoviesRoomTestData.fakeRoomMovies))

    }

    @Test
    fun `should insert return false when data is not inserted`(){
        whenever(movieDao.insert(any())).thenReturn(listOf())
        assertEquals(false, dataSource.insertMovies(FakeMoviesRoomTestData.fakeRoomMovies))
    }

    @Test
    fun `should update return true when data is updated`(){
        whenever(movieDao.update(any())).thenReturn(1)
        assertEquals(true, dataSource.updateMovie(FakeMoviesRoomTestData.fakeRoomMovies[0]))
    }

    @Test
    fun `should update return false when data is not updated`(){
        whenever(movieDao.update(any())).thenReturn(-1)
        assertEquals(false, dataSource.updateMovie(FakeMoviesRoomTestData.fakeRoomMovies[0]))

    }

}