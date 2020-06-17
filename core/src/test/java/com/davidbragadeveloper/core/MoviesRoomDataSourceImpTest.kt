package com.davidbragadeveloper.core

import com.davidbragadeveloper.core.database.dao.MovieDao
import com.vp.detail.localdatasource.DetailsLocalDataSource
import com.davidbragadeveloper.core.datasource.MovieslRoomDataSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoviesRoomDataSourceImpTest{

    private val movieDao: MovieDao = mock()
    private lateinit var  dataSource : DetailsLocalDataSource

    @Before
    fun setup(){
        dataSource = MovieslRoomDataSource(movieDao)
    }



    @Test
    fun `should insert return true when data is inserted`(){
        whenever(movieDao.insert(any())).thenReturn(1)
        assertEquals(true, dataSource.insertMovie(FakeMoviesRoomTestData.fakeMovies[0]))

    }

    @Test
    fun `should insert return false when data is not inserted`(){
        whenever(movieDao.insert(any())).thenReturn(-1)
        assertEquals(false, dataSource.insertMovie(FakeMoviesRoomTestData.fakeMovies[0]))
    }

    @Test
    fun `should update return true when data is updated`(){
        whenever(movieDao.update(any())).thenReturn(1)
        assertEquals(true, dataSource.updateMovie(FakeMoviesRoomTestData.fakeMovies[0]))
    }

    @Test
    fun `should update return false when data is not updated`(){
        whenever(movieDao.update(any())).thenReturn(-1)
        assertEquals(false, dataSource.updateMovie(FakeMoviesRoomTestData.fakeMovies[0]))

    }

}