package com.vp.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListViewModel @Inject constructor(private val searchService: SearchService) : ViewModel() {

    private val liveData: MutableLiveData<SearchResult> = MutableLiveData()
    private var currentTitle: String? = ""
    private val aggregatedItems = mutableListOf<ListItem>()

    fun observeMovies() = liveData

    fun refreshMovies(title: String) {
        searchMoviesByTitle(title, 1, true)
    }

    fun searchMoviesByTitle(title: String, page: Int) {
        searchMoviesByTitle(title, page, false)
    }

    private fun searchMoviesByTitle(title: String, page: Int, forceRefresh: Boolean) {

        val refresh = forceRefresh || page == 1 && title != currentTitle
        if (refresh) {
            aggregatedItems.clear()
            currentTitle = title
            liveData.value = SearchResult.inProgress()
        }

        searchService.search(title, page).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
                response?.body()?.search?.let { aggregatedItems.addAll(it) }
                liveData.value = SearchResult.success(aggregatedItems, aggregatedItems.size)
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                liveData.value = SearchResult.error()
            }
        })
    }
}