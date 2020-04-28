package com.example.diagnal.feature.main

import android.content.Context
import android.util.Log
import com.example.diagnal.data.movie.MovieListResponseModel
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MovieListRepository(private val mContext: Context) {

    fun getMovieList():MovieListResponseModel? {
        val json: String?
        try {
            val inputStream: InputStream = mContext.assets.open("CONTENTLISTINGPAGE-PAGE1.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            val gson = Gson()
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
            val movieListResponseModel = json.let {
                gson.fromJson(it, MovieListResponseModel::class.java)
            }
            Log.d("MovieListRepository", movieListResponseModel.toString())
            return movieListResponseModel
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}