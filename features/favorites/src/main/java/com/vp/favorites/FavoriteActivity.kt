package com.vp.favorites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.vp.favorites.viewmodel.FavoriteViewModel
import androidx.lifecycle.ViewModelProviders
import com.vp.database.entity.FavoriteEntity


class FavoriteActivity : AppCompatActivity() {

    private lateinit var mFavoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        mFavoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
    }
}
