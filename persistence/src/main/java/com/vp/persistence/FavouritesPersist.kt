package com.vp.persistence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.Keep
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

@Keep
data class FavouriteMovie(
        val imdbId: String,
        val title: String?,
        val director: String?,
        val year: String?
)

interface FavouritesDao {
    fun isFavourite(imdbId: String): Boolean
    fun removeFavourite(imdbId: String): Boolean
    fun addFavourite(movie: FavouriteMovie): Boolean
    fun listFavourites(): List<FavouriteMovie>
}

class FavouritesDaoFile(private val context: Context) : FavouritesDao {

    private val gson = Gson()

    override fun isFavourite(imdbId: String): Boolean {
        return try {
            val input = context.openFileInput(filename)
            val buf = BufferedReader(InputStreamReader(input))
            val movies = gson.fromJson(buf, Array<FavouriteMovie>::class.java)
            buf.close()
            movies.find { it.imdbId == imdbId }?.let { true } ?: false
        } catch (e: FileNotFoundException) {
            false
        }
    }

    override fun removeFavourite(imdbId: String): Boolean {
        try {
            val input = context.openFileInput(filename)
            val buf = BufferedReader(InputStreamReader(input))
            val movies = gson.fromJson(buf, Array<FavouriteMovie>::class.java)
            buf.close()

            val moviesList = movies.toMutableList()
            moviesList.remove(moviesList.find { it.imdbId == imdbId })

            val stringOutput = gson.toJson(moviesList.toTypedArray())

            val output = context.openFileOutput(filename, MODE_PRIVATE)
            output.write(stringOutput.toByteArray())
            output.close()
            return true
        } catch (e: FileNotFoundException) {
            return false
        }

    }

    override fun addFavourite(movie: FavouriteMovie): Boolean {
        var movies: Array<FavouriteMovie> = emptyArray()
        try {
            val input = context.openFileInput(filename)
            val buf = BufferedReader(InputStreamReader(input))
            movies = gson.fromJson(buf, Array<FavouriteMovie>::class.java)
            buf.close()
        } catch (e: FileNotFoundException) {
            // TODO: check for file existence instead
        }

        val moviesList = movies.toMutableList()
        moviesList.add(movie)
        val stringOutput = gson.toJson(moviesList.toTypedArray())

        val output = context.openFileOutput(filename, MODE_PRIVATE)
        output.write(stringOutput.toByteArray())
        output.close()
        return true
    }

    override fun listFavourites(): List<FavouriteMovie> {
        return try {
            val input = context.openFileInput(filename)
            val buf = BufferedReader(InputStreamReader(input))
            val movies = gson.fromJson(buf, Array<FavouriteMovie>::class.java)
            buf.close()
            movies.toList()
        } catch (e: FileNotFoundException) {
            emptyList()
        }
    }

    companion object {
        private const val filename = "favourites.json"
    }


}

