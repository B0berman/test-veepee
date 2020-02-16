package saha.tushar.common.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface FavMovieDao {

    @Insert(onConflict = REPLACE)
    fun addFavourite(favMovie: FavMovie) : Long

    @Query("SELECT * from $DB_MOVIE_FAV_TABLE")
    fun getAllFavourites(): LiveData<List<FavMovie>>
}