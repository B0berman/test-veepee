package com.vp.list.di;

import com.vp.db.MovieDao;
import com.vp.db.MovieDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class ListDBModule {

    @Provides
    MovieDao movieDAO(MovieDatabase database) {
        return database.movieDao();
    }
}
