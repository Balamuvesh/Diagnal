package com.example.diagnal.feature.main

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diagnal.R
import com.example.diagnal.data.movie.Movie
import kotlinx.android.synthetic.main.item_title.view.*

class MovieAdapter(private val mContext: Context) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val movieList:MutableList<Movie> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        movieList.get(position).let { holder.bind(it) }
    }

    fun addMovies(list: List<Movie>){
        movieList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        movieList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.tv_title_name.text = movie.name
            Glide.with(mContext)
                .load(getImage(movie.poster_image))
                .fitCenter()
                .placeholder(R.drawable.placeholder_for_missing_posters)
                .into(itemView.img_title_poster)
        }


        private fun getImage(imageName: String?): Int {
            imageName?.substringBefore('.').let {
                return mContext.resources.getIdentifier(it, "drawable", mContext.packageName)
            }
        }
    }
}


class MarginItemDecoration(private val span: Int,private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {        with(outRect) {
        val position = parent.getChildAdapterPosition(view)
        if (position in 0..span) {
            top = spaceHeight
        }
        if(position%span == 0)
            right = spaceHeight

        if((position+1)%span == 0)
            left = spaceHeight
        bottom = spaceHeight*2
    }
    }
}