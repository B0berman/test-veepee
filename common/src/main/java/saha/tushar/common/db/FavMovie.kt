package saha.tushar.common.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import saha.tushar.common.db.DB_COL_IMDB_ID
import saha.tushar.common.db.DB_COL_TITLE
import saha.tushar.common.db.DB_COL_YEAR
import saha.tushar.common.db.DB_MOVIE_FAV_TABLE
import saha.tushar.common.db.DB_COL_RUNTIME
import saha.tushar.common.db.DB_COL_DIRECTOR
import saha.tushar.common.db.DB_COL_PLOT
import saha.tushar.common.db.DB_COL_POSTER

@Entity(tableName = DB_MOVIE_FAV_TABLE)
data class FavMovie(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        @ColumnInfo(name = DB_COL_IMDB_ID) var imdbId: String,
        @ColumnInfo(name = DB_COL_TITLE) var title: String,
        @ColumnInfo(name = DB_COL_YEAR) var year: String,
        @ColumnInfo(name = DB_COL_RUNTIME) var runtime: String,
        @ColumnInfo(name = DB_COL_DIRECTOR) var director: String,
        @ColumnInfo(name = DB_COL_PLOT) var plot: String,
        @ColumnInfo(name = DB_COL_POSTER) var poster: String
)