package com.vp.favorites.model

import androidx.lifecycle.MutableLiveData
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import com.vp.list.viewmodel.SearchResult
import com.vp.storage.DataBaseManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FavouriteListDataRepository @Inject constructor(
        private val dataBaseManager: DataBaseManager,
        private val listItemMapper: ListItemMapper
) {

    val liveData = MutableLiveData<DatabaseResult>()

    fun fetchData() :Disposable {
        liveData.value = DatabaseResult.fetching()
        return dataBaseManager.getMovieDatabase().movieDoa().getAll().toObservable().doOnNext {
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            liveData.value = if (it.isEmpty()) DatabaseResult.empty() else DatabaseResult.fetched(it.map { item -> listItemMapper.fromMovieItemDTO(item) })
        }) {
            liveData.value = DatabaseResult.empty()
        }
    }

}