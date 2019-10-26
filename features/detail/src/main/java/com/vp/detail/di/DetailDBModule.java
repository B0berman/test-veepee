package com.vp.detail.di;

import com.vp.db.MovieDao;
import com.vp.db.MovieDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailDBModule {

    @Provides
    MovieDao movieDAO(MovieDatabase database) {
        return database.movieDao();
    }
}
