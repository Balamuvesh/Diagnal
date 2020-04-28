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
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal.R
import com.example.diagnal.common.extensions.hideKeyboard
import com.example.diagnal.common.extensions.showKeyboard
import com.example.diagnal.common.ui.ViewState
import com.example.diagnal.data.movie.Movie
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {
    private lateinit var mContext: Context

    private var currentPage: Int = 1
    private val misLastPage = false
    private val totalPage = 3
    private var misLoading = false
    var itemCount = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private val movieListRepository by lazy {
        MovieListRepository(mContext)
    }
    private val movieListViewModel by lazy {
        ViewModelProvider(this, MovieListViewModelFactory(movieListRepository)).get(
            MovieListViewModel::class.java
        )
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
                when (viewState ?: ViewState.LOADING) {
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

        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && s.length >= 3) {
                    movieListViewModel.searchMovieList(s.toString())
                }
                Log.d("MainFragmentAfter", " s= $s")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(
                    "MainFragmentBefore",
                    " s= $s   start = $start  count = $count  after = $after"
                )
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(
                    "MainFragmentonChanged",
                    " s= $s   start = $start  count = $count  before = $before"
                )
                if (count == 2 && before > count) {
                    movieListViewModel.searchMovieList(null)
                }
            }

        })



        setupViews()

        movieListViewModel.getMoviesList()
    }

    private fun setupViews() {
        val columns = resources.getInteger(R.integer.rv_movies_span)
        rv_movie_list.addItemDecoration(MarginItemDecoration(columns, 20))
        val gridLayoutManager =
            GridLayoutManager(mContext, columns, GridLayoutManager.VERTICAL, false)
        rv_movie_list.layoutManager = gridLayoutManager

        rv_movie_list.addOnScrollListener(object : PaginationListener(gridLayoutManager) {
            override fun loadMoreItems() {
                misLoading = true
                currentPage += 1
                doApiCall()
            }

            override val isLastPage: Boolean
                get() = misLastPage
            override val isLoading: Boolean
                get() = misLoading
        })

        img_search.setOnClickListener {
            img_search.visibility = View.GONE
            appbar_title.visibility = View.GONE
            search_view.visibility = View.VISIBLE
            movieListViewModel.setIsSearching(true)
            search_view.requestFocus()
            if (search_view.requestFocus()) {
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

    private fun doApiCall() {
        val items = mutableListOf<Movie>()
        movieListViewModel.getMovieListPage()
    }

    private fun populateList(movieList: List<Movie>) {
        val listAdapter = MovieAdapter(mContext, movieList)
        rv_movie_list.adapter = listAdapter
    }
}


abstract class PaginationListener(private val layoutManager: GridLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract val isLastPage: Boolean
    abstract val isLoading: Boolean

    companion object {
        const val PAGE_START = 1

        /**
         * Set scrolling threshold here (for now i'm assuming 10 item in one page)
         */
        private const val PAGE_SIZE = 20
    }

}