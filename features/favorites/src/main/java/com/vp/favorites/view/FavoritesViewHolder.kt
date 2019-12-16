package com.vp.favorites.view

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.muhammedsafiulazam.photoalbum.feature.albumlist.listener.IFavoritesListener
import com.vp.favorites.R
import com.vp.movies.db.Movie
import javax.sql.DataSource

/**
 * Created by Muhammed Safiul Azam on 19/11/2019.
 */

class FavoritesViewHolder(view: View, listener: IFavoritesListener) : RecyclerView.ViewHolder(view){
    private var mView: View
    private var mTxvTitle: AppCompatTextView
    private var mPgbLoader: ProgressBar
    private var mImvThumbnail: AppCompatImageView
    private var mMovie: Movie? = null

    init {
        mView = view
        mTxvTitle = view.findViewById(R.id.favorites_item_txv_title)
        mPgbLoader = view.findViewById(R.id.favorites_item_pgb_loader)
        mImvThumbnail = view.findViewById(R.id.favorites_item_imv_poster)
        mPgbLoader.visibility = View.GONE

        view.setOnClickListener {
            listener.onClickMovie(mMovie!!)
        }
    }

    fun bind(movie: Movie) {
        mMovie = movie

        mTxvTitle.text = movie.title
        mImvThumbnail.setImageDrawable(null)
        mPgbLoader.visibility = View.VISIBLE

        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.dontAnimate()
        requestOptions.dontTransform()

        Glide.with(mView.context)
                .applyDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(movie.poster)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        mPgbLoader.visibility = View.GONE
                        mImvThumbnail.scaleType = ImageView.ScaleType.CENTER
                        mImvThumbnail.setImageResource(R.drawable.ic_cloud_off_black)
                        return true
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        mPgbLoader.visibility = View.GONE
                        mImvThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
                        mImvThumbnail.setImageBitmap(resource)
                        return true
                    }
                })
                .submit()
    }
}