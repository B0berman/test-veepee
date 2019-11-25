package com.vp.list

import android.app.Application
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.robolectric.Shadows.shadowOf
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

/* Robolectric sample test with the AndroidX Testing Library - CE */


class MovieListActivityTest : BaseTest() {
    private lateinit var activityScenario : ActivityScenario<MovieListActivity>

    @Before
    fun setUps() {
        val call = getSearchCallResponseSuccess<SearchResponse>(SearchResponse::class.java)

        Mockito.`when`(mockedSearchService.search(Mockito.anyString(), Mockito.anyInt())).thenReturn(call)

        activityScenario = ActivityScenario.launch(MovieListActivity::class.java)
    }

    @Test
    fun someTest() {
        activityScenario.onActivity {
            assertThat(it).isNotNull()
            val movieFragment = getFragmentInActivity(ListFragment::class.java)
            assertThat(movieFragment).isNotNull()

            val recyclerView = movieFragment.view!!.findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter

            assertThat(adapter!!.itemCount).isEqualTo(6)
        }
    }

    @Test
    fun pressingAnItemShouldStartDetailActivity() {

        activityScenario.onActivity {
            val movieFragment = getFragmentInActivity(ListFragment::class.java)

            val firstItemView = movieFragment.view!!.findViewById<RecyclerView>(R.id.recyclerView).findViewHolderForAdapterPosition(0) as ListAdapter.ListViewHolder

            firstItemView.itemView.performClick()

            val nextStartedActivity = shadowOf(ApplicationProvider.getApplicationContext<Application>()).nextStartedActivity

            assertThat(nextStartedActivity.action).isEqualTo(Intent.ACTION_VIEW)
            assertThat(nextStartedActivity.data).isNotNull()
            // We could optionally assert on the id too as we know the items provided by the mock
            assertThat(nextStartedActivity.data!!.toString()).startsWith("app://movies/detail/?")
        }

    }

    private fun <T>getSearchCallResponseSuccess(classType : Class<*>) : Call<T> {
        val gson = Gson()

        val itemList = generateListItem(6, gson)
        val jsonResponse = "{ Search:" + gson.toJson(itemList) + ", totalResults:" + itemList.size + ", response:True }"
        val searchResponseResponse = Response.success(gson.fromJson(jsonResponse, classType))

        return Calls.response(searchResponseResponse) as Call<T>
    }

    private fun generateListItem(numberOfItem: Int, gson: Gson): List<ListItem> {
        val itemList = mutableListOf<ListItem>()

        for (i in 0 until numberOfItem) {
            val listItemJson = "{\"Title\":\"title $i\", \"Years\":\"2019\", \"Poster\":\"poster $i\"}"
            itemList.add(gson.fromJson(listItemJson, ListItem::class.java))
        }

        return itemList
    }

    private fun getFragmentInActivity(fragClass : Class<out Fragment>) : Fragment {

        var frag : Fragment? = null

        activityScenario.onActivity {
            it.supportFragmentManager.fragments.forEach { fragment ->
                if(fragment.javaClass.isAssignableFrom(fragClass)) {
                    frag = fragment
                }
            }
        }

        if(null == frag)
            throw IllegalArgumentException("No frag found")

        return frag!!
    }

}