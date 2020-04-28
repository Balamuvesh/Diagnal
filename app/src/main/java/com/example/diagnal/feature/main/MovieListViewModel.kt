package com.example.diagnal.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.MovieListResponseModel

class MovieListViewModel(private val movieListRepository: MovieListRepository): ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    val  movieListResponseModel = MutableLiveData<MovieListResponseModel>()
    val errorMsg = MutableLiveData<String>()


    fun getMoviesList(){
        viewState.value = ViewState.LOADING
        movieListRepository.getMovieList().apply {
            movieListResponseModel.value = this
            viewState.value
        }
        viewState.value = ViewState.LOADED
    }
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieListRepository: MovieListRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieListRepository) as T
    }

}