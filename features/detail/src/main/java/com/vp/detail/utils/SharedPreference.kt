package com.vp.detail.utils

import android.content.Context
import android.content.SharedPreferences


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vp.detail.model.MovieDetail

class SharedPreference(private val context: Context) {

    private val gson = Gson()

    companion object {

        private const val KEY_LIST = "lisyOfMovieID"
        private const val PREFS_NAME = "veepee"
        private var map: HashMap<String, MovieDetail>? = HashMap<String, MovieDetail>()


        private fun getSharedPref(context: Context): SharedPreferences {

            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }


    }


    fun save(KEY_NAME: String, myObject: MovieDetail) {

        map?.set(KEY_NAME, myObject)
        editListOfMovie(getSharedPref(context).edit())

    }

    fun getValueMovie(KEY_NAME: String): MovieDetail? {

        if (map?.size == 0)
            map = getListId()

        return map?.get(KEY_NAME)

    }

    fun getListId(): HashMap<String, MovieDetail>? {


        val json = getSharedPref(context).getString(KEY_LIST, null)

        val turnsType = object : TypeToken<HashMap<String, MovieDetail>>() {}.type
        if (json != null)
            map = Gson().fromJson<HashMap<String, MovieDetail>>(json, turnsType)

        if (map == null) {
            map = HashMap<String, MovieDetail>()
        }

        return map

    }


    fun removeValue(KEY_NAME: String) {

        map?.remove(KEY_NAME)
        editListOfMovie(getSharedPref(context).edit())
    }

    private fun editListOfMovie(editor: SharedPreferences.Editor) {

        val jsonList = gson.toJson(map)
        editor.putString(KEY_LIST, jsonList)

        editor.commit()
    }


}

