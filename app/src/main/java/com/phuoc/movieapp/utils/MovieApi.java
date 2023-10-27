package com.phuoc.movieapp.utils;

import com.phuoc.movieapp.models.MovieModel;
import com.phuoc.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //
    // https://api.themoviedb.org/3/search/movie?query=Jack+Reacher&api_key=cf9b63c6c2d7120059db9d5861fc5d41

    // Search for movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("query") String query,
            @Query("api_key") String key,
            @Query("page") int page
    );

    // https://api.themoviedb.org/3/movie/popular?api_key=cf9b63c6c2d7120059db9d5861fc5d41&page=2
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );

    // https://api.themoviedb.org/3/movie/505?api_key=cf9b63c6c2d7120059db9d5861fc5d41
    // Search with ID
    @GET("/3/movie/{movie_id}")
    Call<MovieModel> getMovie (
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );
}
