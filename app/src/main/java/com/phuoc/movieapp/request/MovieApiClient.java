package com.phuoc.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.phuoc.movieapp.AppExecutors;
import com.phuoc.movieapp.models.MovieModel;
import com.phuoc.movieapp.response.MovieSearchResponse;
import com.phuoc.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    // LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies;

    // LiveData for popular
    private MutableLiveData<List<MovieModel>> mMoviesPop;

    // making Global RUNNABLE Popular
    private RetriveMoviesRunnablePop retriveMoviesRunnablePop;


    private static MovieApiClient instance;

    // making Global RUNNABLE
    private RetriveMoviesRunnable retriveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }


    // 1-
    public void searchMovieApi(String query, int pageNumber) {
        if (retriveMoviesRunnable != null) {
            retriveMoviesRunnable = null;
        }

        retriveMoviesRunnable = new RetriveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retriveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviePop(int pageNumber) {
        if (retriveMoviesRunnablePop != null) {
            retriveMoviesRunnablePop = null;
        }

        retriveMoviesRunnablePop = new RetriveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retriveMoviesRunnablePop);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);

    }

    // Retrieving data from RestAPI by runnable class
    // We have 2 types of Queries: the ID & search queries
    private class RetriveMoviesRunnable implements Runnable {
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetriveMoviesRunnable(String query, int pageNumber) {
            this.pageNumber = pageNumber;
            this.query = query;
            this.cancelRequest = false;
        }

        @Override
        public void run() {

            // Getting the response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to livedata
                        // PostValue: used for background thread
                        // SetValue: not for background thread
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }

                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }



        }

        // Search Method/ query
        private Call<MovieSearchResponse> getMovies (String query, int pageNumber) {
            return Services.getMovieApi().searchMovie(
                    query,
                    Credentials.API_KEY,
                    pageNumber
            );
        }


        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetriveMoviesRunnablePop implements Runnable {
        private int pageNumber;
        boolean cancelRequest;

        public RetriveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects
            try {
                Response response = getPop(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to livedata
                        // PostValue: used for background thread
                        // SetValue: not for background thread
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }

                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMoviesPop.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }



        }

        // Search Method/ query
        private Call<MovieSearchResponse> getPop (int pageNumber) {
            return Services.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );

        }


        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}
