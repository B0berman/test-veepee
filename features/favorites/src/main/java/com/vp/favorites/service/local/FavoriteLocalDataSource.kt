package com.vp.favorites.service.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vp.favorites.model.MovieItem
import com.vp.favorites.service.FavoriteDataSource
import kotlinx.coroutines.*

class FavoriteLocalDataSource(private val movieDao: MovieDao, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : FavoriteDataSource {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val mMovieItem = MutableLiveData<MovieItem>()

    private val mMovieItemList = MutableLiveData<List<MovieItem>>()

    override fun subscribe() {
        resetLiveData()
    }

    private fun resetLiveData(){
        mMovieItem.postValue(null)
        mMovieItemList.postValue(null)
    }

    override fun unsubscribe() {
        resetLiveData()
    }

    override suspend fun saveMovieItem(movieItem: MovieItem) = withContext(ioDispatcher) {
        val insertedIndex = movieDao.saveFavorite(movieItem)
        if (insertedIndex >= 0) {
            mMovieItem.postValue(movieItem)
        } else {
            mMovieItem.postValue(null)
        }
    }

    override suspend fun deleteMovieItem(imdbID: String) {
        val deletedIndex = movieDao.deleteMovieItemById(imdbID)
        if (deletedIndex >= 0) {
            mMovieItem.postValue(null)
        }
    }

    override suspend fun loadFavorites() {
        val data = movieDao.getMovieItems()
        mMovieItemList.postValue(data)
    }

    override fun observeMovieItem(): LiveData<MovieItem> {
        return mMovieItem
    }

    override fun observeMovieItemList(): LiveData<List<MovieItem>> {
        return mMovieItemList
    }

    override suspend fun refreshSelectedFavorite(imdbID: String) = withContext(ioDispatcher) {
        val result = movieDao.getMovieItemById(imdbID)
        if (result != null) {
            mMovieItem.postValue(result)
        } else {
            mMovieItem.postValue(result)
        }
    }
}