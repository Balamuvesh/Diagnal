package com.example.diagnal.feature.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diagnal.R
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.data.movie.MovieListResponseModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private val movieListRepository by lazy {
        MovieListRepository(mContext)
    }
    private val movieListViewModel by lazy {
        ViewModelProvider(this, CustomViewModelFactory(movieListRepository)).get(MovieListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieListViewModel.movieListResponseModel.observe(
            viewLifecycleOwner,
            Observer<MovieListResponseModel> { movieListResponseModel ->
                populateView(movieListResponseModel.page.contentItems.movieList)
            })

        movieListViewModel.getMoviesList()
    }


    private fun populateView(movieList: List<Movie>) {
        val gridLayoutManager = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        rv_movie_list.layoutManager = gridLayoutManager
        val listAdapter = MovieAdapter(mContext, movieList)
        rv_movie_list.adapter = listAdapter
        rv_movie_list.addItemDecoration(MarginItemDecoration(20))
    }
}