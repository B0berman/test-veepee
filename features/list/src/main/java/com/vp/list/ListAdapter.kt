package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.poster

class ListAdapter(private val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

  private var listItem = mutableListOf<ListItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_list, parent, false)
    return ViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindArticle(listItem.elementAt(position))
  }

  override fun getItemCount() = listItem.size

  internal fun setItems(items: MutableList<ListItem>) {
    this.listItem = items
    notifyDataSetChanged()
  }

  fun clearItems() {
    this.listItem.clear()
  }

  class ViewHolder(view: View, private val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindArticle(item: ListItem) {
      with(item) {
        if (!poster.isNullOrEmpty() && NO_IMAGE != poster) {
          val density = itemView.poster.resources.displayMetrics.density
          com.vp.common.GlideApp
              .with(itemView.poster)
              .load(poster)
              .override((300 * density).toInt(), (600 * density).toInt())
              .into(itemView.poster)
        } else {
          itemView.poster.setImageResource(R.drawable.placeholder);
        }
        itemView.setOnClickListener { itemClick(this.imdbID) }
      }
    }
  }

  companion object {
    private const val NO_IMAGE = "N/A"
  }
}