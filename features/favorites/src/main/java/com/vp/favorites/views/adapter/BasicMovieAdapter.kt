package com.vp.favorites.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.R
import com.vp.favorites.model.BasicMovie
import kotlinx.android.synthetic.main.view_item_list.view.*


class BasicMovieAdapter(
    private val context: Context,
    private var data: List<BasicMovie>
)  : RecyclerView.Adapter<BasicMovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateItems(list: List<BasicMovie>) {
        data = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: BasicMovie) {
            with(view.card) {
                setTitle(data.title)
                setImage(data.poster)
            }
        }
    }
}