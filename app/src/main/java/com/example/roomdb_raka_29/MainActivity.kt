package com.example.roomdb_raka_29

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb_raka_29.room.Constant
import com.example.roomdb_raka_29.room.Movie
import com.example.roomdb_raka_29.room.MovieDb
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    val db by lazy { MovieDb(this) }
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        setupListener()
        setupRecyclerView()



    }

    override fun onStart() {
        super.onStart()

        loadMovie()

    }

    fun loadMovie(){
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.MovieDao().getMovies()
            Log.d("Main Activity", "dbresponse: $movies")
            withContext(Dispatchers.Main){
                movieAdapter.setData(movies)
            }
        }
    }

    fun setupListener() {
        val add_movie = findViewById<Button>(R.id.add_movie)

        add_movie.setOnClickListener {
            intentAdd(0, Constant.TYPE_CREATE)
        }
    }

    fun intentAdd(movieId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, AddActivity::class.java)
                .putExtra("intent_id", movieId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener{
            override fun onClick(movie: Movie) {
                //read detail
                intentAdd(movie.id, Constant.TYPE_READ)
            }

            override fun onUpdate(movie: Movie) {
                intentAdd(movie.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                deleteDialog(movie)
            }
        })
        rv_movie.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }

    private fun deleteDialog(movie: Movie){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${movie.title}?")
            setNegativeButton("Batal",) {dialoginterface, i ->
                dialoginterface.dismiss()
            }

            setPositiveButton("Hapus",) {dialoginterface, i ->
                dialoginterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.MovieDao().deleteMovie(movie)
                    loadMovie()
                }
            }
        }

        alertDialog.show()
    }

}