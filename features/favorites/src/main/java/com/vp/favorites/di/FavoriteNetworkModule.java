package com.vp.favorites.di;


import com.vp.favorites.service.FetchService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class FavoriteNetworkModule {

    @Provides
    FetchService providesSearchService(Retrofit retrofit) {
        return retrofit.create(FetchService.class);
    }
}
