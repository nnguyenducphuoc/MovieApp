package com.phuoc.movieapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phuoc.movieapp.R;
import com.phuoc.movieapp.models.MovieModel;
import com.phuoc.movieapp.utils.Credentials;

import java.util.List;

public class MovieRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static  final  int DISPLAY_POP = 1;
    private static  final  int DISPLAY_SEARCH = 2;

    public MovieRecycleView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == DISPLAY_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder)holder).title.setText(mMovies.get(position).getTitle());
        ((MovieViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());
        ((MovieViewHolder)holder).duration.setText(mMovies.get(position).getOriginal_language());

        ((MovieViewHolder)holder).ratingBar.setRating(mMovies.get(position).getVote_average()/2);
        Glide.with(((MovieViewHolder) holder).imageView.getContext())
                .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(position).getPoster_path())
                .into(((MovieViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    // Getting the id of the movie clicked
    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return  null;
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR) {
            return DISPLAY_POP;
        } else {
            return DISPLAY_SEARCH;
        }
    }
}
