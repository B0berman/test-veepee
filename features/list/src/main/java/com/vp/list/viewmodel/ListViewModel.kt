package com.vp.list.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService

import java.util.ArrayList

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel @Inject constructor(private val searchService: SearchService) : ViewModel() {

    companion object {
        val TAG = "ListViewModel"
    }

    private val liveData = MutableLiveData<SearchResult>()
    private val totalMovies = MutableLiveData<Int>()

    private var currentTitle = ""
    private val aggregatedItems = ArrayList<ListItem>()

    fun observeMovies(): LiveData<SearchResult> = liveData

    fun totalMovies(): LiveData<Int> = totalMovies

    fun searchMoviesByTitle(title: String, page: Int, isRefreshing: Boolean = false) {

        if ((page == 1 && title != currentTitle) || isRefreshing) {
            aggregatedItems.clear()
            currentTitle = title
            totalMovies.value = 0
            liveData.value = SearchResult.inProgress()
        }
        searchService.search(title, page).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                response.body()?.let {
                    aggregatedItems.addAll(it.search?.asIterable() ?: emptyList())
                    liveData.value = SearchResult.success(aggregatedItems, it.totalResults)
                    if (page == 1) {
                        totalMovies.value = it.totalResults
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                liveData.value = SearchResult.error()
            }
        })
    }
}
