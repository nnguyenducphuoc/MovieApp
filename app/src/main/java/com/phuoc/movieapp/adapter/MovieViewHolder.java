package com.phuoc.movieapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuoc.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title, release_date, duration;
    ImageView imageView;
    RatingBar ratingBar;

    // Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;

        title = itemView.findViewById(R.id.movie_title);
        release_date = itemView.findViewById(R.id.movie_category);
        duration = itemView.findViewById(R.id.movie_duration);

        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);

        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
