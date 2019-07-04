package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.viewmodel.model.FavoriteMovie
import kotlinx.android.synthetic.main.item_favorite_movie.view.*
import kotlinx.coroutines.*

internal class FavoriteAdapter(
        private val onShow: (FavoriteMovie) -> Unit,
        private val onRemove: (FavoriteMovie) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(), LifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var moviesList: List<FavoriteMovie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_favorite_movie, parent, false)
                .let { view -> ViewHolder(view) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int = moviesList.size

    fun submitList(list: List<FavoriteMovie>) {
        scope.launch(Dispatchers.Default) {
            val result = DiffUtil.calculateDiff(
                    FavoriteDiffCallback(
                            oldList = moviesList,
                            newList = list
                    )
            )
            launch(Dispatchers.Main) {
                moviesList = list
                result.dispatchUpdatesTo(this@FavoriteAdapter)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        scope.cancel()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val resources = view.resources
        private val poster = view.poster
        private val title = view.title
        private val year = view.year
        private val director = view.director
        private val star = view.star

        fun bind(movie: FavoriteMovie) {
            itemView.setOnClickListener { onShow(movie) }
            star.setImageResource(R.drawable.ic_star_full)
            star.setOnClickListener {
                star.setImageResource(R.drawable.ic_star_empty)
                onRemove(movie)
            }
            title.text = movie.title
            year.text = resources.getString(R.string.year_template, movie.year)
            director.text = resources.getString(R.string.director_template, movie.director)

            if (movie.poster != null) {
                Glide.with(poster)
                        .load(movie.poster)
                        .error(R.drawable.placeholder)
                        .into(poster)
            } else {
                poster.setImageResource(R.drawable.placeholder)
            }
        }
    }
}