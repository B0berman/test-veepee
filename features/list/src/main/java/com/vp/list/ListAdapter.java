package com.vp.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.vp.list.model.ListItem;
import java.util.Collections;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BaseListViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private static final int VIEW_TYPE_LOADING_MORE = 1;
    private List<ListItem> listItems = Collections.emptyList();
    private final OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = imdbID -> {
        //empty listener
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
    private boolean isLoadingMore = false;

    @NonNull
    @Override
    public BaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_LOADING_MORE) {
            return new LoadingMoreViewHolder(inflater.inflate(
                R.layout.item_loading_more,
                parent,
                false
            ));
        } else {
            return new ListViewHolder(inflater.inflate(R.layout.item_list, parent, false));
        }
    }

    private boolean isLoadingMoreItem(int position) {
        return isLoadingMore && position == listItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseListViewHolder holder, int position) {
        if (!isLoadingMoreItem(position)) {
            ListItem listItem = listItems.get(position);
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            if (listItem.getPoster() != null && !NO_IMAGE.equals(listItem.getPoster())) {
                final float density = listViewHolder.image.getResources()
                    .getDisplayMetrics().density;
                GlideApp
                    .with(listViewHolder.image)
                    .load(listItem.getPoster())
                    .override((int) (300 * density), (int) (600 * density))
                    .into(listViewHolder.image);
            } else {
                listViewHolder.image.setImageResource(R.drawable.placeholder);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingMoreItem(position)) {
            return VIEW_TYPE_LOADING_MORE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size() + (isLoadingMore ? 1 : 0);
    }

    public void setItems(List<ListItem> listItems, boolean isLoadingMore) {
        this.listItems = listItems;
        this.isLoadingMore = isLoadingMore;
        notifyDataSetChanged();
    }

    public void clearItems() {
        listItems.clear();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
        }
    }

    public int getItemSpanSize(int position, int defaultSpanCount) {
        if (isLoadingMoreItem(position)) {
            return defaultSpanCount;
        } else {
            return 1;
        }
    }

    abstract static class BaseListViewHolder extends RecyclerView.ViewHolder {

        BaseListViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ListViewHolder extends BaseListViewHolder implements View.OnClickListener {
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.poster);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
        }
    }

    static class LoadingMoreViewHolder extends BaseListViewHolder {
        LoadingMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
