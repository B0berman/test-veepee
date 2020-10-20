package com.vp.list.di

import com.vp.list.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ListFragmentModule {

    @ContributesAndroidInjector
    fun bindListFragment(): ListFragment
}
