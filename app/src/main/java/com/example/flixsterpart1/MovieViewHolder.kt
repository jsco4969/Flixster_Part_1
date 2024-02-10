package com.example.flixsterpart1

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    fun bind(movie: Movie) {
        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val posterImageView = itemView.findViewById<ImageView>(R.id.posterImageView)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.descriptionTextView)


        titleTextView.text = movie.title
        descriptionTextView.text = movie.overview


        Glide.with(itemView)
            .load(movie.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(posterImageView)
    }
}

