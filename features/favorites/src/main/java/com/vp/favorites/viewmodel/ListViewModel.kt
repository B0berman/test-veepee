package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.viewmodel.FavoriteFetchResult.Companion.inProgress
import com.vp.favorites.viewmodel.FavoriteFetchResult.Companion.onSuccess
import com.vp.persistence.storage.AppPreferences
import javax.inject.Inject

class ListViewModel @Inject internal constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val liveData = MutableLiveData<FavoriteFetchResult>()
    private var currentTitle = ""

    fun observeMovies(): LiveData<FavoriteFetchResult> {
        return liveData
    }

    fun searchMoviesByTitle(title: String, page: Int) {
        if (page == 1 && title != currentTitle) {
            currentTitle = title
            liveData.value = inProgress()
        }
        appPreferences.getFCMToken()
        liveData.value = onSuccess(
            listOf()
        )

//        searchService.search(title, page).enqueue(object : Callback<SearchResponse?>() {
//            fun onResponse(
//                call: Call<SearchResponse?>,
//                response: Response<SearchResponse?>
//            ) {
//                val result: SearchResponse = response.body()
//                if (result != null) {
//                    aggregatedItems.addAll(result.getSearch())
//                    liveData.setValue(
//                        success(
//                            result.getSearch(),
//                            result.getTotalResults()
//                        )
//                    )
//                }
//            }
//
//            fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
//                liveData.setValue(error())
//            }
//        })
    }
}