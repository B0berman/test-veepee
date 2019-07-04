package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.vp.list.service.model.SearchResponse
import com.vp.list.service.SearchService
import com.vp.list.viewmodel.model.ListItem
import com.vp.list.viewmodel.model.ListState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import retrofit2.mock.Calls
import java.io.FileReader
import java.io.IOException

class ListViewModelTest {

    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    private val mapper = ListItemModelMapper()

    @Test
    fun `should return ListState_ERROR`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.submitQuery("title")
        val state = listViewModel.getState().value

        //then
        assertThat(state).isEqualTo(ListState.ERROR)
    }

    @Test
    fun `should return ListState_IN_PROGRESS`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock()))
        val listViewModel = ListViewModel(searchService)
        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")

        //then
        verify(mockObserver).onChanged(ListState.IN_PROGRESS)
    }

    @Test
    fun `should return ListState_LOADED`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(getResponse(PAGE1)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")

        //then
        verify(mockObserver).onChanged(ListState.LOADED)
    }

    @Test
    fun `should return only empty list on failed request`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.submitQuery("title")
        val items = listViewModel.getItems().value

        //then
        assertThat(items).isEqualTo(emptyList<ListItem>())
    }

    @Test
    fun `should return valid list on successful request`() {
        //given
        val searchService: SearchService = mock()
        val response = getResponse(PAGE1)
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(response))
        val listViewModel = ListViewModel(searchService)
        val expectedList = response.items!!.map { item -> mapper.toListItem(item) }

        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")
        val items = listViewModel.getItems().value

        //then
        assertThat(items).isEqualTo(expectedList)
    }

    @Test
    fun `should return valid aggregated list after 2 successful page loads`() {
        //given
        val searchService: SearchService = mock()
        val response1 = getResponse(PAGE1)
        val response2 = getResponse(PAGE2)
        `when`(searchService.search(anyString(), eq(1))).thenReturn(Calls.response(response1))
        `when`(searchService.search(anyString(), eq(2))).thenReturn(Calls.response(response2))
        val listViewModel = ListViewModel(searchService)
        val expectedList = (response1.items!! + response2.items!!).map { item -> mapper.toListItem(item) }

        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")
        listViewModel.load(2)
        val items = listViewModel.getItems().value

        //then
        assertThat(items).isEqualTo(expectedList)
    }

    @Test
    fun `should return has more pages after 1 successful page load`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(getResponse(PAGE1)))
        val listViewModel = ListViewModel(searchService)

        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")
        val hasMorePages = listViewModel.hasMorePages().value

        //then
        assertThat(hasMorePages).isEqualTo(true)
    }

    @Test
    fun `should return has no more pages after 2 successful page loads`() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), eq(1))).thenReturn(Calls.response(getResponse(PAGE1)))
        `when`(searchService.search(anyString(), eq(2))).thenReturn(Calls.response(getResponse(PAGE2)))
        val listViewModel = ListViewModel(searchService)

        val mockObserver: Observer<ListState> = mock()
        listViewModel.getState().observeForever(mockObserver)

        //when
        listViewModel.submitQuery("title")
        listViewModel.load(2)
        val hasMorePages = listViewModel.hasMorePages().value

        //then
        assertThat(hasMorePages).isEqualTo(false)
    }

    private fun getResponse(path: String): SearchResponse {
        val fileReader = FileReader(javaClass.classLoader!!.getResource(path).path)
        val jsonReader = JsonReader(fileReader)
        return Gson().fromJson(jsonReader, SearchResponse::class.java)
    }

    private companion object {
        const val RESPONSES_DIR = "responses"
        const val PAGE1 = "$RESPONSES_DIR/SearchResponse_page1_successful.json"
        const val PAGE2 = "$RESPONSES_DIR/SearchResponse_page2_successful.json"
    }
}