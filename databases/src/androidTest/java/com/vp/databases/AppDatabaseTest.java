package com.vp.databases;

import android.content.Context;

import com.vp.databases.model.FavoriteMovie;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private FavoriteMovieDao favoriteMovieDao;
    private AppDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        favoriteMovieDao = db.favoriteMovieDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        // given
        FavoriteMovie favoriteMovie = new FavoriteMovie(4L, "title", "year", "imdbID", "poster");

        //when
        favoriteMovieDao.insert(favoriteMovie);

        //then
        Observer<List<FavoriteMovie>> mockObserver = (Observer<List<FavoriteMovie>>) mock(Observer.class);
        favoriteMovieDao.getAll().observeForever(mockObserver);

        //then
        verify(mockObserver).onChanged(Collections.singletonList(favoriteMovie));
    }

}
