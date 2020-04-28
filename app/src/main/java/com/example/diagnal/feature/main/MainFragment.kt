package com.example.diagnal.feature.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diagnal.R
import com.example.diagnal.common.extensions.hideKeyboard
import com.example.diagnal.common.extensions.showKeyboard
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import com.example.diagnal.data.movie.MovieListResponseModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_title.*


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
        ViewModelProvider(this, MovieListViewModelFactory(movieListRepository)).get(MovieListViewModel::class.java)
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

        movieListViewModel.viewState.observe(
            viewLifecycleOwner,
            Observer { viewState ->
                when(viewState ?: ViewState.LOADING){
                    ViewState.LOADING -> progress_circular.visibility = View.VISIBLE
                    ViewState.LOADED -> progress_circular.visibility = View.GONE
                    ViewState.ERROR -> TODO()
                }
            }
        )
        movieListViewModel.movieList.observe(
            viewLifecycleOwner,
            Observer<List<Movie>> { movieList ->
                appbar_title.text = movieListViewModel.movieListResponseModel.page.pageTitle
                populateList(movieList)
            })

        search_view.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty() && s.length >= 3){
                    movieListViewModel.searchMovieList(s.toString())
                }
                Log.d("MainFragmentAfter", " s= $s")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("MainFragmentBefore", " s= $s   start = $start  count = $count  after = $after")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("MainFragmentonChanged", " s= $s   start = $start  count = $count  before = $before")
                if(count == 2 && before > count){
                    movieListViewModel.searchMovieList(null)
                }
            }

        })

        setupViews()

        movieListViewModel.getMoviesList()
    }

    private fun setupViews() {

        rv_movie_list.addItemDecoration(MarginItemDecoration(20))


        img_search.setOnClickListener {
            img_search.visibility = View.GONE
            appbar_title.visibility = View.GONE
            search_view.visibility = View.VISIBLE
            movieListViewModel.setIsSearching(true)
            search_view.requestFocus()
            if(search_view.requestFocus()) {
                search_view.showKeyboard()
            }
        }

        /**
         * [search_view] is an EditText with a right drawable. The below code sets a touch listener
         *  to the right drawable.
         */
        search_view.setOnTouchListener(OnTouchListener { v, event ->
//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
//            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= search_view.right - search_view.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    search_view.text = null
                    img_search.visibility = View.VISIBLE
                    appbar_title.visibility = View.VISIBLE
                    search_view.visibility = View.GONE
                    movieListViewModel.setIsSearching(false)
                    search_view.hideKeyboard()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun populateList(movieList: List<Movie>) {
        val gridLayoutManager = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        rv_movie_list.layoutManager = gridLayoutManager
        val listAdapter = MovieAdapter(mContext, movieList)
        rv_movie_list.adapter = listAdapter
    }
}