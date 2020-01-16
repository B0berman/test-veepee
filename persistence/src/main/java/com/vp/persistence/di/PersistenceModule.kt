package com.vp.persistence.di

import com.vp.persistence.storage.AppPreferences
import com.vp.persistence.storage.AppPreferencesStorage
import dagger.Module
import dagger.Provides

@Module
class PersistenceModule {

    @Provides
    fun providesAppPreferences(storage: AppPreferencesStorage): AppPreferences = storage
}