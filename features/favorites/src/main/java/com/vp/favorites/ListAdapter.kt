/*
 * Created by Alexis Rodriguez Paret on 3/13/20 11:11 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 11:11 PM
 *
 */

package com.vp.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.mylibrary.GlideApp
import com.vp.persistance.model.MovieModel

/**
 * Created by Alexis Rodríguez Paret on 2020-03-13.
 */
class ListAdapter(private val list: List<MovieModel>)
    : RecyclerView.Adapter<ListAdapter.MovieViewHolder>() {
    var EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(imdbID: String?) {
            Log.d("ads", "ads")
        }
    }
    private val NO_IMAGE = "N/A"
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: MovieModel = list[position]
        if (movie.poster != null && NO_IMAGE != movie.poster) {
            val density: Float = holder.poster.resources.displayMetrics.density
            GlideApp
                    .with(holder.poster)
                    .load(movie.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.poster)
        } else {
            holder.poster.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }

    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var poster: ImageView = itemView.findViewById(R.id.poster)

        init {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(list[adapterPosition].imdbID)
            }
        }
    }

    override fun onViewRecycled(holder: MovieViewHolder) {
        super.onViewRecycled(holder)
        Glide.get(context).clearMemory()
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }
}
