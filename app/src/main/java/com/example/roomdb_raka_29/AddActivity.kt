package com.example.roomdb_raka_29

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.roomdb_raka_29.room.Constant
import com.example.roomdb_raka_29.room.Movie
import com.example.roomdb_raka_29.room.MovieDao
import com.example.roomdb_raka_29.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy { MovieDb(this) }
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupView()

        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                btn_update.visibility = View.GONE
            }

            Constant.TYPE_READ -> {
                btn_save.visibility = View.GONE
                btn_update.visibility = View.GONE
                getMovie()
            }

            Constant.TYPE_UPDATE -> {
                btn_save.visibility = View.GONE
                getMovie()
            }
        }
    }

    fun setupListener() {

        val btn_save = findViewById<Button>(R.id.btn_save)
        val et_title = findViewById<EditText>(R.id.et_title)
        val et_desc = findViewById<EditText>(R.id.et_desc)

        btn_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MovieDao().addMovie(
                    Movie(0, et_title.text.toString(),
                    et_desc.text.toString())
                )

                finish()
            }
        }

        btn_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MovieDao().updateMovie(
                    Movie(movieId, et_title.text.toString(),
                        et_desc.text.toString())
                )

                finish()
            }
        }

    }

    fun getMovie(){

        val edit_title = findViewById<EditText>(R.id.et_title)
        val edit_desc = findViewById<EditText>(R.id.et_desc)


        movieId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.MovieDao().getMovie(movieId)[0]

            edit_title.setText(movies.title)
            edit_desc.setText(movies.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}