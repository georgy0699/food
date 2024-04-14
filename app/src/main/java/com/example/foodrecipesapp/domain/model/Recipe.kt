package com.example.foodrecipesapp.domain.model

data class Recipe (
    val id: Int,
    val title: String,
    val description: String,
    val duration: String,
    val imgUrl: String,
    var rating: String,
    val category: String,
    val userId: Int,
    var isFavourite: Boolean,
)