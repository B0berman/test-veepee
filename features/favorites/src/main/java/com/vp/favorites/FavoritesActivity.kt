package com.vp.favorites

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

class FavoritesActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
    }

}