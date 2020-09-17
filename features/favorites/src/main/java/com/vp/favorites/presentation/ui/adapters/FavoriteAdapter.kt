package com.vp.favorites.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.R.layout
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.presentation.ui.adapters.FavoriteAdapter.ViewHolder
import com.vp.common.GlideApp
import kotlinx.android.synthetic.main.favorite_list_item.view.poster

class FavoriteAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var favouriteItem = emptyList<FavoriteItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(layout.favorite_list_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindArticle(favouriteItem.elementAt(position))
  }

  override fun getItemCount() = favouriteItem.size

  internal fun setItems(items: List<FavoriteItem>) {
    this.favouriteItem = items
    notifyDataSetChanged()
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindArticle(item: FavoriteItem) {
      with(item) {
        if (image.isNotEmpty()) {
          val density = itemView.poster.resources.displayMetrics.density
          GlideApp
              .with(itemView.poster)
              .load(image)
              .override((300 * density).toInt(), (600 * density).toInt())
              .into(itemView.poster)
        }
      }
    }
  }
}