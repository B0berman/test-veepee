package com.vp.favorites


import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.database.entity.FavoriteEntity
import com.vp.detail.DetailActivity
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favorite.*
import javax.inject.Inject

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var mFavoriteViewModel: FavoriteViewModel

    private var listAdapter: FavoriteAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        mFavoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        mFavoriteViewModel.getAll()?.observe(this, Observer<List<FavoriteEntity>> {
            favoriteMovies -> if(favoriteMovies != null) {listAdapter?.setItems(favoriteMovies)}
        })
    }

    private fun initList() {
        listAdapter = FavoriteAdapter()
        listAdapter?.setOnItemClickListener(this)
        recyclerView!!.adapter = listAdapter
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView?.layoutManager = layoutManager
    }

    override fun onItemClick(imdbID: String) {
        val builder = Uri.Builder()
        builder.scheme("app")
                .authority("movies")
                .appendPath("detail")
                .appendQueryParameter("imdbID", imdbID)
        val uri = builder.build()

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(requireContext().packageName)

        startActivity(intent)
    }
}
