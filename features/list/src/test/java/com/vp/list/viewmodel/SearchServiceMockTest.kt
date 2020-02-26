package com.vp.list.viewmodel

import com.vp.favorites.model.ListItem
import com.vp.list.BuildConfig
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

class SearchServiceMockTest {
    private var delegate: BehaviorDelegate<SearchService>? = null

    @Before
    fun before() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://example.com")
                .build()
        val mockRetrofit = MockRetrofit.Builder(retrofit)
                .networkBehavior(NetworkBehavior.create())
                .build()
        delegate = mockRetrofit.create<SearchService>(SearchService::class.java)
    }

    @Test
    fun testData(){
        delegate?.let {
            val mockSearchService = SearchServiceMock(it)
            val quote: Call<SearchResponse> = mockSearchService.search(BuildConfig.SEARCH_VIEW_INITIAL_WORD, 1)
            val response: Response<SearchResponse> = quote.execute()
            assertThat(response.isSuccessful)
            val searchResponse: SearchResponse? = response.body()
            val totalResults = 1096
            val size = searchResponse?.totalResults ?: 0
            assertThat(size).isEqualTo(totalResults)
            val firstItem: ListItem? = searchResponse?.search?.get(0)
            val mockFirstItem = ListItem("The Interview", "2014", "tt2788710", "https://m.media-amazon.com/images/M/MV5BMTQzMTcwMzgyMV5BMl5BanBnXkFtZTgwMzAyMzQ2MzE@._V1_SX300.jpg")
            assertThat(firstItem?.title).isEqualTo(mockFirstItem.title)
            assertThat(firstItem?.year).isEqualTo(mockFirstItem.year)
            assertThat(firstItem?.imdbID).isEqualTo(mockFirstItem.imdbID)
            assertThat(firstItem?.poster).isEqualTo(mockFirstItem.poster)
            val mockLastItem = ListItem("The Michael Jackson Interview: The Footage You Were Never Meant to See", "2003", "tt0361907", "https://m.media-amazon.com/images/M/MV5BNGUwOTk5NDAtOTA4Yy00NzM0LThiYTktMTJkM2MyNmU0ODBjXkEyXkFqcGdeQXVyMTAwMjY1Mw@@._V1_SX300.jpg")
            val lastItem: ListItem? = searchResponse?.search?.get(9)
            assertThat(lastItem?.title).isEqualTo(mockLastItem.title)
            assertThat(lastItem?.year).isEqualTo(mockLastItem.year)
            assertThat(lastItem?.imdbID).isEqualTo(mockLastItem.imdbID)
            assertThat(lastItem?.poster).isEqualTo(mockLastItem.poster)
        }
    }
}
