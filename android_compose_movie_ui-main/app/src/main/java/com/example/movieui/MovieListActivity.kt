package com.example.movieui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class MovieListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        // Initialize Firebase in this activity
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        val picasso = Picasso.Builder(applicationContext)
            // Add any customization here if needed
            .build()
        Picasso.setSingletonInstance(picasso)

        val movieList: MutableList<Movie> = mutableListOf()
        db.collection("movies")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val imageUrl = document.getString("thumbnail") ?: ""
                    val rating = document.getString("rating") ?: ""
                    val duration = document.getString("duration") ?: ""
                    val summary = document.getString("summary") ?: ""

                    // Create a Movie object and add it to the movieList
                    val movie = Movie(title, imageUrl, rating, duration, summary)
                    movieList.add(movie)
                }

                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)

                movieAdapter = MovieAdapter(movieList, this)
                recyclerView.adapter = movieAdapter

            }
            .addOnFailureListener { exception -> }
    }
}
