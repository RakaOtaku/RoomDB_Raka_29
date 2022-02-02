package com.example.roomdb_raka_29.room

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val desc: String
        )