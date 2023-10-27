package com.phuoc.movieapp.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.phuoc.movieapp.models.MovieModel;

// This class
public class MovieResponse {
    // 1. Finding the Movie Object
    @SerializedName("results")
    @Expose()
    private MovieModel movie;

    public MovieModel getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
