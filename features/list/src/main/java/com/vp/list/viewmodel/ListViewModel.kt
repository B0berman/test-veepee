package com.vp.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import com.vp.list.viewmodel.SearchResult.Companion.error
import com.vp.list.viewmodel.SearchResult.Companion.inProgress
import com.vp.list.viewmodel.SearchResult.Companion.success
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class ListViewModel @Inject internal constructor(private val searchService: SearchService) : ViewModel() {
  private val liveData = MutableLiveData<SearchResult>()
  private var currentTitle = ""
  private val aggregatedItems: MutableList<ListItem> = ArrayList()

  fun observeMovies() = liveData

  fun searchMoviesByTitle(title: String, page: Int) {
    if (page == 1 && title != currentTitle) {
      aggregatedItems.clear()
      currentTitle = title
      liveData.value = inProgress()
    }
    searchService.search(title, page).enqueue(object : Callback<SearchResponse?> {
          override fun onResponse(
            call: Call<SearchResponse?>, response: Response<SearchResponse?>
          ) {
            response.body()?.let {
              if (it.hasResponse()) {
                aggregatedItems.addAll(it.search!!)
                liveData.value = success(aggregatedItems, it.totalResults)
              }
            }
          }

          override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
            liveData.value = error()
          }
        })
  }
}