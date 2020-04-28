package com.example.diagnal.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.data.movie.MovieListResponseModel

class MovieListViewModel(private val movieListRepository: MovieListRepository) : ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    val movieListResponseModel = MutableLiveData<MovieListResponseModel>()
    val errorMsg = MutableLiveData<String>()
    private var initialMoviesList: List<Movie> = listOf()

    //Set this parameter to true to start searching
    private var isSearching: Boolean = false


    fun getMoviesList() {
        viewState.value = ViewState.LOADING
        movieListRepository.getMovieList().apply {
            movieListResponseModel.value = this
        }
        viewState.value = ViewState.LOADED
    }

    fun searchMovieList(query: String?) {
        //if query is  null, reset list to initial value
        if (query == null)
            movieListResponseModel.value?.page?.contentItems?.movieList = initialMoviesList
        else
            movieListResponseModel.value?.page?.contentItems?.movieList = initialMoviesList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        movieListResponseModel.value = movieListResponseModel.value
    }

    fun setIsSearching(boolean: Boolean) {
        if (boolean) {
            if (!isSearching)
                movieListResponseModel.value?.page?.contentItems?.movieList?.let {
                    initialMoviesList = it
                }
        } else {
            movieListResponseModel.value?.page?.contentItems?.movieList = initialMoviesList
            movieListResponseModel.value = movieListResponseModel.value
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieListRepository: MovieListRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieListRepository) as T
    }

}