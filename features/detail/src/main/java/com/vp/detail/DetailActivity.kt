package com.vp.detail

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.run
import android.content.SharedPreferences
import android.R.id.edit





class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        System.out.println("DetailActivity id "+intent?.data?.getQueryParameter("imdbID") );
        /*
        val data = this.intent.data
        if (data != null && data.isHierarchical) {
            if (data.getQueryParameter("imdbID") != null) {
                val param1 = data.getQueryParameter("imdbID")
                // do some stuff
            }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        val item = menu!!.findItem(R.id.star)

        item.setOnMenuItemClickListener {
            val sharedPreferences = getSharedPreferences(getString(R.string.favorite), Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            var fav =sharedPreferences.getString(intent?.data?.getQueryParameter("imdbID"), "")
            if(fav == "") {
                fav = "1"
            }else{
                fav = ""
            }
            var a = intent?.data?.getQueryParameter("imdbID");
            editor.putString(a, fav)
            editor.commit()
            true
        }

        return true
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
