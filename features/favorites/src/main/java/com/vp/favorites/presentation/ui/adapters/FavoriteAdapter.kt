package com.vp.favorites.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.R.layout
import com.vp.favorites.domain.model.FavoriteItem
import com.vp.favorites.presentation.ui.adapters.FavoriteAdapter.ViewHolder
import kotlinx.android.synthetic.main.favorite_list_item.view.favoriteImage
import kotlinx.android.synthetic.main.favorite_list_item.view.favoriteTitle

class FavoriteAdapter (private val itemClick: (FavoriteItem) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

  private var favouriteItem = emptyList<FavoriteItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
        layout.favorite_list_item, parent, false)
    return ViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindArticle(favouriteItem.elementAt(position))
  }

  override fun getItemCount() = favouriteItem.size

  internal fun setItems(items: List<FavoriteItem>) {
    this.favouriteItem = items
    notifyDataSetChanged()
  }

  class ViewHolder(view: View, private val itemClick: (FavoriteItem) -> Unit) :
      RecyclerView.ViewHolder(view) {
    fun bindArticle(item: FavoriteItem) {
      with(item) {
        itemView.favoriteTitle.text = title

        if(image.isNotEmpty()) {
          //TODO use glide to show image
        }
        itemView.setOnClickListener { itemClick(this) }
      }
    }
  }
}