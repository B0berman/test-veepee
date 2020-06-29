package com.vp.favorite.service

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class SharedPreferencesFavoritesServiceTest {
    val prefs: SharedPreferences = mock()
    val edit: SharedPreferences.Editor = mock()
    val change: FavChangeCallback = mock()

    @Test
    fun `should load all favorite ids`() {
        //given
        val expected = listOf("id", "id1")
        whenever(prefs.all).thenReturn(mapOf("id" to true, "id1" to true))
        val service = SharedPreferencesFavoritesService(prefs)

        //when
        val actual = service.getAllFavorite()

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun `should save as favorite`() {
        //given
        val id = "id"
        whenever(prefs.edit()).thenReturn(edit)
        whenever(edit.putBoolean(id, true)).thenReturn(edit)
        val service = SharedPreferencesFavoritesService(prefs)

        //when
        service.save(id, true)

        //then
        verify(edit).putBoolean(id, true)
    }

    @Test
    fun `should save as not favorite`() {
        //given
        val id = "id"
        whenever(prefs.edit()).thenReturn(edit)
        whenever(edit.remove(id)).thenReturn(edit)
        val service = SharedPreferencesFavoritesService(prefs)

        //when
        service.save(id, false)

        //then
        verify(edit).remove(id)
    }

    @Test
    fun `should listen for changes`() {
        //given
        val id = "id"
        whenever(prefs.getBoolean(id, false)).thenReturn(true)
        whenever(prefs.registerOnSharedPreferenceChangeListener(any())).thenAnswer {
            val callback = it.arguments[0] as SharedPreferences.OnSharedPreferenceChangeListener
            callback.onSharedPreferenceChanged(prefs, id)
            Unit
        }
        val service = SharedPreferencesFavoritesService(prefs)

        //when
        service.listenId(id, change)

        //then
        verify(change, times(2)).invoke(true)
        verify(prefs).registerOnSharedPreferenceChangeListener(any())
        verify(prefs, never()).unregisterOnSharedPreferenceChangeListener(any())
    }

    @Test
    fun `should stop listen for changes`() {
        //given
        val id = "id"
        whenever(prefs.getBoolean(id, false)).thenReturn(true)
        whenever(prefs.registerOnSharedPreferenceChangeListener(any())).thenAnswer {
            val callback = it.arguments[0] as SharedPreferences.OnSharedPreferenceChangeListener
            callback.onSharedPreferenceChanged(prefs, id)
            Unit
        }
        val service = SharedPreferencesFavoritesService(prefs)

        //when
        service.listenId(id, change).invoke()

        //then
        verify(prefs).registerOnSharedPreferenceChangeListener(any())
        verify(prefs).unregisterOnSharedPreferenceChangeListener(any())
    }
}