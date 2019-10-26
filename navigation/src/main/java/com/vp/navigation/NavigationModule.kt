package com.vp.navigation

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
object NavigationModule {

    @JvmStatic
    @Provides
    internal fun navigation(activity: Activity): NavigationHelper = NavigationHelper(activity)
}