package com.vp.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ListViewModel @Inject internal constructor(private val searchService: SearchService) : ViewModel() {
    val movies = MutableLiveData<SearchResult>()

    private var currentTitle = ""
    private val aggregatedItems = ArrayList<ListItem>()

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            aggregatedItems.clear()
            currentTitle = title
            movies.value = SearchResult.inProgress()
        }
        searchService.search(title, page).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                response.body()?.also {
                    aggregatedItems.addAll(it.search)
                    movies.value = SearchResult.success(aggregatedItems, it.totalResults)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                movies.value = SearchResult.error()
            }
        })
    }
}
