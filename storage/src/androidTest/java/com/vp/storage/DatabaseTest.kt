package com.vp.storage

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vp.storage.database.MovieDatabase
import com.vp.storage.database.MoviesDao
import com.vp.storage.model.MovieDetailDB
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db: MovieDatabase
    private lateinit var dao: MoviesDao

    @Before
    fun createDB() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
                appContext, MovieDatabase::class.java).build()
        dao = db.moviesDao()
    }

    @Test
    fun writeMovieAndReadInList() = runBlocking {
        val testMovie = MovieDetailDB(imdbID = "testID")
        dao.insertMovie(testMovie)
        val dbMovie = dao.findMovieByTitle("testID")
        assertEquals(dbMovie, testMovie)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}
