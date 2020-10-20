package com.vp.favorites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity(R.layout.activity_favorite), HasAndroidInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector() = dispatchingActivityInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
