package com.vp.list.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.service.SearchService
import com.vp.list.service.model.SearchResponse
import com.vp.list.viewmodel.model.ListItem
import com.vp.list.viewmodel.model.ListState
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.properties.Delegates

class ListViewModel @Inject internal constructor(
        private val searchService: SearchService
) : ViewModel() {

    private val items = MutableLiveData<List<ListItem>>()
    private val hasMorePages = MutableLiveData<Boolean>()
    private val state = MutableLiveData<ListState>()
    private val mapper = ListItemModelMapper()

    private var query: String by Delegates.observable("") { _, oldValue, newValue ->
        if (oldValue != newValue) {
            call?.cancel()
            items.value = emptyList()

            if (newValue.isNotBlank()) {
                load(1)
            }
        }
    }

    private var call: Call<SearchResponse>? = null

    fun getItems(): LiveData<List<ListItem>> = items

    fun hasMorePages(): LiveData<Boolean> = hasMorePages

    fun getState(): LiveData<ListState> = state

    @MainThread
    fun submitQuery(title: String) {
        query = title
    }

    @MainThread
    fun refresh() {
        load(1)
    }

    @MainThread
    fun load(page: Int) {
        call?.cancel()
        state.value = ListState.IN_PROGRESS
        call = searchService
                .search(query, page)
                .also { call -> call.enqueue(getCallback(page)) }
    }

    private fun getCallback(page: Int) = object : Callback, retrofit2.Callback<SearchResponse> {
        override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
            response.body()
                    ?.let { result ->
                        val resultItems = result
                                .items
                                ?.map { item -> mapper.toListItem(item) }
                                ?: emptyList()

                        val list = if (page == 1) {
                            resultItems
                        } else {
                            (items.value ?: emptyList()) + resultItems
                        }

                        state.value = ListState.LOADED
                        items.value = list
                        hasMorePages.value = list.size < result.totalResults ?: 0
                    }
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            state.value = ListState.ERROR
        }
    }
}
