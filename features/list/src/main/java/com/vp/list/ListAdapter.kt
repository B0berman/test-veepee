package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.ListAdapter.ListViewHolder
import com.vp.list.model.ListItem

private const val NO_IMAGE = "N/A"
class ListAdapter(
        private inline val onItemClick : ((imdbID: String) -> Unit)? = null
) : RecyclerView.Adapter<ListViewHolder>() {

    private var listItems: List<ListItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
            LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_list, parent, false)
    )


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            GlideApp
                    .with(holder.image)
                    .load(listItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount() = listItems.size

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems = emptyList()
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView

        init {
            itemView.setOnClickListener{ onItemClick?.invoke(listItems[adapterPosition].imdbID) }
            image = itemView.findViewById(R.id.poster)
        }
    }

}