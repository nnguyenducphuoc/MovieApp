package com.phuoc.movieapp.request;

import com.phuoc.movieapp.utils.Credentials;
import com.phuoc.movieapp.utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Services {
    // https://api.themoviedb.org/3/search/movie?query=Jack+Reacher&api_key=cf9b63c6c2d7120059db9d5861fc5d41
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Credentials.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    public static MovieApi getMovieApi() {
        if (movieApi == null) {
            movieApi = retrofit.create(MovieApi.class);
        }
        return movieApi;
    }
}
