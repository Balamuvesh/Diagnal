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

class MovieAdapter(private val mContext: Context, private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.tv_title_name.text = movie.name
            Glide.with(mContext)
                .load(getImage(movie.poster_image))
                .fitCenter()
                .placeholder(getImage("placeholder_for_missing_posters"))
                .into(itemView.img_title_poster)
        }

        private fun getImage(imageName: String?): Int {
            return mContext.resources.getIdentifier(imageName, "drawable", mContext.packageName)
        }
    }
}


class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {        with(outRect) {
        if (parent.getChildAdapterPosition(view) == 0) {
            top = spaceHeight
        }
        left =  spaceHeight
        right = spaceHeight
        bottom = spaceHeight*3
    }
    }
}