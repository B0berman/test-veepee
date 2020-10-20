package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class ListViewModel @Inject internal constructor(
    private val searchService: SearchService
) : ViewModel() {
    private var currentTitle = ""
    private val aggregatedItems: MutableList<ListItem> = ArrayList()

    private val _movies = MutableLiveData<SearchResult>()
    val movies: LiveData<SearchResult> = _movies

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
            _movies.setValue(SearchResult.inProgress())
        } else {
            _movies.setValue(SearchResult.loadingMore(aggregatedItems))
        }
        executeSearchQuery(title, page)
    }

    private fun executeSearchQuery(title: String, page: Int) {
        searchService.search(title, page).enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>, response: Response<SearchResponse?>
            ) {
                val result = response.body()
                if (result != null) {
                    aggregatedItems.addAll(result.search)
                    val searchResult = SearchResult.success(aggregatedItems, result.totalResults)
                    _movies.value = searchResult
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                _movies.value = SearchResult.error()
            }
        })
    }

    fun refreshMovies() {
        aggregatedItems.clear()
        _movies.value = SearchResult.inProgress()
        executeSearchQuery(currentTitle, 1)
    }
}
