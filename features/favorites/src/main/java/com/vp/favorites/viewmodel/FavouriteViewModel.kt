package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tushar.todosample.db.FavMovieDb
import saha.tushar.common.db.FavMovie
import saha.tushar.common.db.FavMovieRepository

class FavouriteViewModel : ViewModel() {
    private lateinit var dbFavMovieRepository: FavMovieRepository

    fun init(favMovieDb: FavMovieDb) {
        dbFavMovieRepository = FavMovieRepository(favMovieDb)
    }

    fun getFavList(): LiveData<List<FavMovie>> = dbFavMovieRepository.getFavourites()
}