package com.vp.moviedatabase.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.*


class FavoriteMovieDaoTest {
    private lateinit var database: com.vp.moviedatabase.data.AppDatabase
    private lateinit var dao: com.vp.moviedatabase.data.FavoriteMovieDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java).build()
        dao = database.favoriteMovieDao()
    }

    @Test
    fun insertion() {
        val expectedMovie = oneFavoriteMovie
        dao.insert(expectedMovie)
        val actualMovie = dao.getMovie(oneFavoriteMovie.id)
        assertThat(expectedMovie,equalTo(actualMovie))
    }

    @Test
    fun givenMovieIsNotInDbWhenUsingItsIdThenReturnNull() {
        Assert.assertNull(dao.getMovie("123"))
    }

    @Test
    fun deletion() {
        val expectedMovie = oneFavoriteMovie
        val movieId = oneFavoriteMovie.id
        dao.insert(expectedMovie)
        dao.deleteMovie(movieId)
        Assert.assertNull(dao.getMovie(movieId))
    }

    @Test
    fun resolutionPolicyShouldBeReplace() {
        dao.insert(oneFavoriteMovie)
        val expectedTitle = "New Title"
        val movieWithSameIdButDifferentValues = Movie(oneFavoriteMovie.id,
                expectedTitle,
                oneFavoriteMovie.plot,
                oneFavoriteMovie.director,
                oneFavoriteMovie.runtime,
                oneFavoriteMovie.year,
                oneFavoriteMovie.posterId)
        dao.insert(movieWithSameIdButDifferentValues)
        assertThat(dao.getMovie(oneFavoriteMovie.id)?.title, equalTo(expectedTitle))

    }

    @After
    fun deinit() {
        database.close()
    }
}