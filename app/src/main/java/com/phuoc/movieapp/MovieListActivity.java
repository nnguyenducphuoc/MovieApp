package com.phuoc.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.phuoc.movieapp.adapter.MovieRecycleView;
import com.phuoc.movieapp.adapter.OnMovieListener;
import com.phuoc.movieapp.models.MovieModel;

import com.phuoc.movieapp.viewmodel.MovieListViewModel;


import java.util.List;


public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecycleView movieRecycleViewAdapter;
    // ViewModel
    private MovieListViewModel movieListViewModel;
    
    boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycle_view);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        // Calling the observers
        ObserveAnyChange();

        ObservePopularMovies();

        // getting popular movies
        movieListViewModel.searchMovieApi( "", 1);
        SetupSearchView();
        
    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null) {
                    for (MovieModel movieModel: movieModels) {
                        // get the data
                        movieRecycleViewAdapter.setMovies(movieModels);
                    }
                }
            }
        });
    }

    private void SetupSearchView() {

        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string getted from search view
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });

    }

    // Observing any data change
    private void ObserveAnyChange() {

        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null) {
                    for (MovieModel movieModel: movieModels) {
                        // get the data
                        movieRecycleViewAdapter.setMovies(movieModels);
                    }
                }

            }
        });


    }


//    private void GetRetrofitResponse() {
//        MovieApi movieApi = Services.getMovieApi();
//
//        Call<MovieSearchResponse> responseCall = movieApi
//                .searchMovie(
//                        Credentials.API_KEY,
//                        "1");
//
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if (response.code() == 200) {
//                    Log.v("Tag", "the response " + response.body().toString());
//
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//
//                    for (MovieModel movie : movies) {
//                        Log.v("Tag", "The title " + movie.getTitle());
//                    }
//                } else {
//                    try {
//                        Log.v("Tag", "Error " + response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//
//            }
//        });
//    }
//    private void GetRetrofitResponseAccordingtoID() {
//        MovieApi movieApi = Services.getMovieApi();
//
//        Call<MovieModel> responseCall = movieApi
//                .getMovie(505,
//                        Credentials.API_KEY);
//
//        responseCall.enqueue((new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                                if (response.code() == 200) {
//                    MovieModel movie = response.body();
//
//                    Log.v("Tag", "The Response " + movie.getTitle());
//                } else {
//                    try {
//                        Log.v("Tag", "Error " + response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//
//            }
//        }));
//
//
//
//        // (MovieResponse) Get search name
////        Call<MovieResponse> responseCall = movieApi
////                .getMovie(505,
////                Credentials.API_KEY);
////
////        responseCall.enqueue(new Callback<MovieResponse>() {
////            @Override
////            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
////
////                if (response.code() == 200) {
////                    Log.v("Tag", "ff");
////                    Log.v("Tag", "The Response " + response.body());
////                    Log.v("Tag", "dd");
////                    MovieModel movie = new MovieModel(
////                            response.body().getMovie().getTitle(),
////                            response.body().getMovie().getPoster_path(),
////                            response.body().getMovie().getRelease_date(),
////                            response.body().getMovie().getMovie_id(),
////                            response.body().getMovie().getVote_average(),
////                            response.body().getMovie().getOverview());
////
////                    Log.v("Tag", "cc");
////                    Log.v("Tag", "The movie " + movie.getTitle());
////                    Log.v("Tag", "gg");
////
////
////                } else {
////                    try {
////                        Log.v("Tag", "Error " + response.errorBody().string());
////                    } catch (IOException e) {
////                        throw new RuntimeException(e);
////                    }
////                }
////
////            }
////
////            @Override
////            public void onFailure(Call<MovieResponse> call, Throwable t) {
////
////            }
////        });
//    }

    // 5- Intializing recycleview & adding data to it
    private void ConfigureRecyclerView() {
        movieRecycleViewAdapter = new MovieRecycleView(this);
        recyclerView.setAdapter(movieRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        // RecyclerView Pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    // Here we need to display the next search results on the next page of api
                    movieListViewModel.searchNextPage();


                }
            }
        });


    }

    @Override
    public void onMovieClick(int position) {
    //    Toast.makeText(this, "The position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecycleViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }
}