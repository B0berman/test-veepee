package com.vp.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.vp.common.GlideApp;
import com.vp.favorites.model.ListItem;

import java.util.Collections;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_LOADING = 0;
    private static final int VIEW_LOADED = 1;
    private static final int VIEW_ERROR = 2;
    private static final String NO_IMAGE = "N/A";
    private List<ListItem> listItems = Collections.emptyList();
    private OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = imdbID -> {
        //empty listener
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_LOADING:
                return new LoaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item_list, parent, false));
            case VIEW_LOADED:
                return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
            case VIEW_ERROR:
                return new ErrorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.error_item_list, parent, false));
            default:
                return new LoaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item_list, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_LOADING:
                break;
            case VIEW_LOADED:
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

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ListItem listItem = listItems.get(position);
        // Choose the listitem layout depending the status of the movie detail
        switch (listItem.getStatus()) {
            case LOADING:
                return VIEW_LOADING;
            case LOADED:
                return VIEW_LOADED;
            case ERROR:
                return VIEW_ERROR;
            default:
                return VIEW_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
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

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
        }
    }

    class LoaderViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progress;

        LoaderViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progressBar);
        }
    }

    class ErrorViewHolder extends RecyclerView.ViewHolder {
        ErrorViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
