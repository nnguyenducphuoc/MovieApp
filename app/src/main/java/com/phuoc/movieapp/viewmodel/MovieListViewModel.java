package com.phuoc.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.phuoc.movieapp.models.MovieModel;
import com.phuoc.movieapp.repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    // Constructor
    private MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPop() {
        return movieRepository.getPop();
    }
    // 3 Calling method in view-model
    public void searchMovieApi(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber) {
        movieRepository.searchMoviePop(pageNumber);
    }

    public void searchNextPage() {
        movieRepository.searchNextPage();
    }
}
