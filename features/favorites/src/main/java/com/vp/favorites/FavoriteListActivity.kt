package com.vp.favorites

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class FavoriteListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, FavoriteListFragment(), FavoriteListFragment.TAG)
                    .commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingActivityInjector
    }
}
