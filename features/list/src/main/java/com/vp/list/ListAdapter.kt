package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.viewmodel.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(
        private val onItemClick: (imdbID: String) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var items: List<ListItem> = emptyList()
        set(value) {
            val previous = field
            field = value
            if (value != previous) notifyDataSetChanged()
        }

    private var morePagesToLoad: Boolean = false
        set(value) {
            val previous = field
            field = value
            if (value != previous) notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_MOVIE -> MovieViewHolder(
                    inflater.inflate(R.layout.item_list, parent, false)
            )
            VIEW_PROGRESS -> ListViewHolder(
                    inflater.inflate(R.layout.item_progress, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_MOVIE) {
            (holder as MovieViewHolder).bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return if (morePagesToLoad && items.isNotEmpty()) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= items.size) VIEW_PROGRESS else VIEW_MOVIE
    }

    fun hasItems() = items.isNotEmpty()

    fun setHasMorePages(hasMorePages: Boolean) {
        morePagesToLoad = hasMorePages
    }

    fun getSpanSizeLookup(maxColumns: Int) = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return (if (position >= items.size) maxColumns else 1)
        }
    }

    open class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class MovieViewHolder(itemView: View) : ListViewHolder(itemView), View.OnClickListener {
        private val image: ImageView = itemView.poster

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) = onItemClick(items[adapterPosition].imdbID)

        fun bind(listItem: ListItem) {
            if (listItem.poster != null && NO_IMAGE != listItem.poster) {
                val density = image.resources.displayMetrics.density
                GlideApp
                        .with(image)
                        .load(listItem.poster)
                        .error(R.drawable.placeholder)
                        .override((300 * density).toInt(), (600 * density).toInt())
                        .into(image)
            } else {
                image.setImageResource(R.drawable.placeholder)
            }
        }
    }

    companion object {
        private const val VIEW_MOVIE = 0
        private const val VIEW_PROGRESS = 1

        private const val NO_IMAGE = "N/A"
    }
}
