package com.vp.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri

class NavigationHelper(
        private val activity: Activity
) {

    fun launchDetail(imdbID: String) {
        val uri = Uri.parse("app://movies/detail?imdbID=$imdbID")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(activity.packageName)
        activity.startActivity(intent)
    }

    fun launchFavorites() {
        val uri = Uri.parse("app://movies/favorites")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(activity.packageName)
        activity.startActivity(intent)
    }
}