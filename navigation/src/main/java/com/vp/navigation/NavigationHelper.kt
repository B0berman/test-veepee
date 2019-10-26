package com.vp.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri

class NavigationHelper(
        private val activity: Activity
) {

    fun launchDetail(imdbID: String) {
        val uri = Uri.parse("app://movies/detail?imdbID=$imdbID")
        activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}