package com.example.movieui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: List<Movie>, private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card_layout, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        holder.itemView.findViewById<Button>(R.id.buy_ticket_button).setOnClickListener {
            // Handle the click event for Buy Ticket button here
            val intent = Intent(context, SeatingActivity::class.java)
            // Add any extras if needed
            // intent.putExtra("movieId", movie.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImage: ImageView = itemView.findViewById(R.id.movie_image)
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val movieRating: TextView = itemView.findViewById(R.id.movie_rating)
        private val movieDuration: TextView = itemView.findViewById(R.id.movie_duration)
        private val movieSummary: TextView = itemView.findViewById(R.id.movie_summary)

        fun bind(movie: Movie) {
            // Set data to views
            movieTitle.text = movie.title
            movieRating.text = "rating: " + movie.rating
            movieDuration.text = movie.duration + " mins"
            movieSummary.text = movie.summary

            if (movie.imageUrl.isNotEmpty()) { // Check if the image URL is not empty
                Picasso.get()
                    .load(movie.imageUrl)
                    .placeholder(R.drawable.default_movie_poster)
                    .error(R.drawable.default_movie_poster)
                    .fit()
                    .centerCrop()
                    .into(movieImage)
            } else {
                // Handle the case when imageUrl is empty or null
                // For example, show a default image or hide the ImageView
                movieImage.setImageResource(R.drawable.default_movie_poster)
            }
        }
    }
}
