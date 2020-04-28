package com.example.diagnal.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.data.movie.MovieListResponseModel

class MovieListViewModel(private val movieListRepository: MovieListRepository) : ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    lateinit var movieListResponseModel: MovieListResponseModel
    val movieList = MutableLiveData<MutableList<Movie>>()
    private var initialMoviesList: MutableList<Movie> = mutableListOf()

    //Set this parameter to true to start searching
    var isSearching: Boolean = false


    fun getMoviesList() {
        viewState.value = ViewState.LOADING
        movieListRepository.getMovieList(1)?.apply {
            movieListResponseModel = this
            movieList.value = this.page.contentItems.movieList as MutableList<Movie>
        }
        viewState.value = ViewState.LOADED
    }

    fun getMovieListPage(pageNumber: Int): MovieListResponseModel?{
       return movieListRepository.getMovieList(pageNumber)
    }

    fun searchMovieList(query: String?) {
        //if query is  null, reset list to initial value
        if (query == null) {
            movieList.value = initialMoviesList
        }else {
            movieList.value = initialMoviesList.filter {
                it.name.contains(query, ignoreCase = true)
            } as MutableList<Movie>
        }
        movieList.value = movieList.value
    }

    fun setIsSearching(boolean: Boolean) {
        if (boolean) {
            if (!isSearching)
                movieList.value?.let {
                    initialMoviesList = it
                }
        } else {
            movieList.value = initialMoviesList
        }
        isSearching = boolean
    }
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieListRepository: MovieListRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieListViewModel(movieListRepository) as T
    }

}