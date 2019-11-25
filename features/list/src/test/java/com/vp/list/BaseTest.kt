package com.vp.list

import androidx.test.core.app.ApplicationProvider
import com.vp.list.service.SearchService
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject


@RunWith(RobolectricTestRunner::class)
@Config(application = TestMovieListApplication::class, sdk = [26])
abstract class BaseTest {


    @Inject
    lateinit var mockedSearchService : SearchService

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        ApplicationProvider.getApplicationContext<TestMovieListApplication>().testAppComponent.inject(this)
    }


}