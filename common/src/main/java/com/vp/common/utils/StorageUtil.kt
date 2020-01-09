package com.vp.common.utils

import android.content.Context
import com.vp.common.R


class StorageUtil {
    companion object {

        fun getFavoriteMoviesList(context: Context): MutableSet<String>? {
            val sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.favorite_preference_file_key), Context.MODE_PRIVATE
            )
            return sharedPreferences.getStringSet(context.getString(R.string.favorite_entry_key), HashSet<String>())
        }

        fun saveFaroritesMovieList(context: Context, movieList: MutableSet<String>) {
            val sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.favorite_preference_file_key), Context.MODE_PRIVATE
            ) ?: return
            with(sharedPreferences.edit()) {
                // Must remove the key (type StringSet) cause the direct override do not persiste the new value
                remove(context.getString(R.string.favorite_entry_key))
                commit()
                putStringSet(context.getString(R.string.favorite_entry_key), movieList)
                commit()
            }
        }
    }
}