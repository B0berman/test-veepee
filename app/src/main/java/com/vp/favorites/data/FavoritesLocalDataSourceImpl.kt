package com.vp.favorites.data

import androidx.lifecycle.LiveData
import com.vp.favorites.datasource.FavoritesLocalDataSource
import com.vp.favorites.model.FavouriteItem
import com.vp.storage.MoviesDatabase
import com.vp.storage.db.asLiveData
import com.vp.storage.db.mapToList
import com.vp.storage.dbmodel.DbMovie

class FavoritesLocalDataSourceImpl(
        private val movieDataBase: MoviesDatabase
) : FavoritesLocalDataSource {

    override fun favorites(): LiveData<List<FavouriteItem>> =
            movieDataBase.moviesQueries.selectAll()
                    .asLiveData()
                    .mapToList(mapper = { dbMovies ->
                        dbMovies.map(DbMovie::toFavouriteItem)
                    })
}
