/*
 * Created by Alexis Rodriguez Paret on 3/13/20 6:22 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 10:09 AM
 *
 */

package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoriteActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun bindFavoriteActivity(): FavoriteActivity
}