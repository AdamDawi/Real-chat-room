package com.example.distancecoupleapp.data

data class Photo(
    val imageUrl: String,
    val owner: String,
    val comments: Map<String, Comment>,
)