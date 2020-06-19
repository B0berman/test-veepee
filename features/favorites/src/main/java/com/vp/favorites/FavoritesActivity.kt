package com.vp.favorites

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

class FavoritesActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, FavoritesFragment(), FavoritesFragment.TAG)
                    .commit()
        }
    }
}
