package com.example.diagnal.feature.main

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diagnal.R
import com.example.diagnal.data.Movie
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: Fragment() {
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
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

        populateView()
    }

    private fun populateView() {
        val tempMoviesList:List<Movie> = listOf(Movie("The Birds", "poster1"), Movie("The Birds", "poster1"), Movie("The Birds", "poster1"), Movie("The Birds", "poster1"), Movie("The Birds", "poster1"), Movie("The Birds", "poster1") )
        val gridLayoutManager = GridLayoutManager(mContext,3, GridLayoutManager.HORIZONTAL, false)
        rv_movie_list.layoutManager = gridLayoutManager
        val listAdapter = MovieAdapter(mContext, tempMoviesList)
        rv_movie_list.adapter = listAdapter
    }
}