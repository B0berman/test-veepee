package com.vp.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vp.list.model.ListItem;

import java.util.Collections;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_DATA = 1;
    public static final int VIEW_TYPE_PROGRESS_BAR = 2;
    private static final String NO_IMAGE = "N/A";
    private List<ListItem> listItems = Collections.emptyList();
    private OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = imdbID -> {
        //empty listener
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
    private boolean isLoading = false;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        } else {
            return new ProgressBarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ListViewHolder) {
            ListItem listItem = listItems.get(position);
            ListViewHolder listViewHolder = (ListViewHolder) holder;
            if (listItem.getPoster() != null && !NO_IMAGE.equals(listItem.getPoster())) {
                final float density = listViewHolder.image.getResources().getDisplayMetrics().density;
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
        return position >= listItems.size() ? VIEW_TYPE_PROGRESS_BAR : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return isLoading ? listItems.size() + 1 : listItems.size();
    }

    public void setItems(List<ListItem> listItems) {
        this.listItems = listItems;
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

    public void showLoadingIndicator(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {

        ProgressBarViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
