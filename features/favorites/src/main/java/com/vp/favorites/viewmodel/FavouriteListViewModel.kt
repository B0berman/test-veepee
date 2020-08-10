package com.vp.favorites.viewmodel

import androidx.lifecycle.*
import com.vp.favorites.model.FavouriteListDataRepository
import com.vp.favorites.model.FavouriteListState
import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FavouriteListViewModel @Inject internal constructor(
        private val dataRepository: FavouriteListDataRepository
) : ViewModel(), LifecycleObserver {

    val favouriteListItems: LiveData<List<ListItem>> = Transformations.map(dataRepository.liveData) { it.items }

    val favouriteListState: LiveData<FavouriteListState> = Transformations.map(dataRepository.liveData) { it.listState }

    private var disposable: Disposable? = null
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        disposable =dataRepository.fetchData()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun destroy() {
       disposable?.run { if (!isDisposed) dispose() }
    }
}