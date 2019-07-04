package com.vp.favorites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vp.detail.model.MovieDetail;
import com.vp.favorites.databinding.ItemMovieBinding;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.HolderView> {

    private List<MovieDetail> list;
    private Activity activity;


    public Adapter( Activity activity) {
        this.list = new ArrayList<MovieDetail>();
        this.activity = activity;
    }

    public  void changeAdapter(List<MovieDetail> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new HolderView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {

        if(list!=null && list.size()>position) {
            final MovieDetail movie = list.get(position);
            holder.bind(movie);
            holder.binding.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Uri uri = Uri.parse("app://movies/detail?imdbID=" + movie.getImdbID());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage(activity.getPackageName());

                    activity.startActivity(intent);
                    activity.finish();


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class HolderView extends RecyclerView.ViewHolder {

        ItemMovieBinding binding;

        public HolderView(@NonNull View itemView) {
            super(itemView);

            this.binding = DataBindingUtil.bind(itemView);
        }


        public void bind(MovieDetail movie) {
            this.binding.setMovie(movie);
            this.binding.executePendingBindings();

        }


    }
}
