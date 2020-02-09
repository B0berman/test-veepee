package com.vp.favorites.service

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vp.favorites.model.FavoriteMovie
import java.util.*

class FavoritesSQLDataBaseHelper constructor(context: Context) : SQLiteOpenHelper(context.applicationContext ?: context, DATA_BASE_NAME, null, DATA_BASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //nothing to do yet
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addFavoriteMovie(id: String, title: String, year: String, runtime: String, director: String, plot: String, poster: String) =
            addFavoriteMovie(FavoriteMovie(id, title, year, runtime, director, plot, poster))

    private fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
        writableDatabase.use { dataBase ->
            val contentValues = favoriteMovie.toContentValues()
            dataBase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE)
        }
    }

    fun removeFromFavoriteMovie(id: String) {
        writableDatabase.use { dataBase ->
            dataBase.execSQL(getRemoveInstruction(id))
        }
    }

    fun exists(id: String) = readableDatabase.use { dataBase ->
        dataBase.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=\"$id\" LIMIT 1", null).use { cursor ->
            cursor.count > 0
        }
    }

    fun getAllFavoriteMovies() = readableDatabase.use { dataBase ->
        dataBase.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            cursor.map { it.toFavoriteMovie() }
        }
    }

    companion object {
        private const val DATA_BASE_NAME = "Movies.Favorites"
        private const val DATA_BASE_VERSION_1 = 1

        private const val DATA_BASE_VERSION = DATA_BASE_VERSION_1

        private const val TABLE_NAME = "favorites"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_RUNTIME = "runtime"
        private const val COLUMN_DIRECTOR = "director"
        private const val COLUMN_PLOT = "plot"
        private const val COLUMN_POSTER = "poster"

        private const val COLUMN_ID_DEFINE = "$COLUMN_ID text primary key"
        private const val COLUMN_TITLE_DEFINE = "$COLUMN_TITLE text"
        private const val COLUMN_YEAR_DEFINE = "$COLUMN_YEAR text"
        private const val COLUMN_RUNTIME_DEFINE = "$COLUMN_RUNTIME text"
        private const val COLUMN_DIRECTOR_DEFINE = "$COLUMN_DIRECTOR text"
        private const val COLUMN_PLOT_DEFINE = "$COLUMN_PLOT text"
        private const val COLUMN_POSTER_DEFINE = "$COLUMN_POSTER text"

        private const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME($COLUMN_ID_DEFINE,$COLUMN_TITLE_DEFINE,$COLUMN_YEAR_DEFINE,$COLUMN_RUNTIME_DEFINE,$COLUMN_DIRECTOR_DEFINE,$COLUMN_PLOT_DEFINE,$COLUMN_POSTER_DEFINE)"

        private const val REMOVE = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID=%s"

        private fun getRemoveInstruction(id: String) = String.format(Locale.US, REMOVE, id)

        private fun FavoriteMovie.toContentValues() = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_TITLE, title)
            put(COLUMN_YEAR, year)
            put(COLUMN_RUNTIME, runtime)
            put(COLUMN_DIRECTOR, director)
            put(COLUMN_PLOT, plot)
            put(COLUMN_POSTER, poster)
        }

        private fun Cursor.toFavoriteMovie() = FavoriteMovie(
                id = getString(getColumnIndex(COLUMN_ID)),
                title = getString(getColumnIndex(COLUMN_TITLE)),
                year = getString(getColumnIndex(COLUMN_YEAR)),
                runtime = getString(getColumnIndex(COLUMN_RUNTIME)),
                director = getString(getColumnIndex(COLUMN_DIRECTOR)),
                plot = getString(getColumnIndex(COLUMN_PLOT)),
                poster = getString(getColumnIndex(COLUMN_POSTER))
        )

        private fun <T> Cursor.map(predicate: (Cursor) -> T) =
                generateSequence { takeIf { moveToNext() } }
                        .map { predicate(it) }
                        .toList()
    }
}
