package com.example.flixsterpart1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter()
        recyclerView.adapter = adapter

        fetchMovies()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchMovies() {
        GlobalScope.launch(Dispatchers.IO) {
            val apiKey = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
            val url = URL("https://api.themoviedb.org/3/movie/now_playing?api_key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append('\n')
                }
                bufferedReader.close()
                val json = stringBuilder.toString()
                parseMovies(json)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching movies", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    private fun parseMovies(json: String) {
        val moviesList = mutableListOf<Movie>()
        val jsonObject = JSONObject(json)
        val resultsArray = jsonObject.getJSONArray("results")
        for (i in 0 until resultsArray.length()) {
            val movieObject = resultsArray.getJSONObject(i)
            val title = movieObject.getString("title")
            val overview = movieObject.getString("overview")
            val posterPath = movieObject.getString("poster_path")
            val imageUrl = "https://image.tmdb.org/t/p/w500/$posterPath"
            val movie = Movie(title, overview, imageUrl)
            moviesList.add(movie)
        }
        runOnUiThread {
            adapter.setMovies(moviesList)
        }
    }
}

