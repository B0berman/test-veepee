package com.vp.list.di.module

import com.vp.list.service.SearchService
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class TestListNetworkModule {

    @Singleton
    @Provides
    fun providesSearchService(retrofit: Retrofit): SearchService = Mockito.mock(SearchService::class.java)

}