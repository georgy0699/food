package com.example.foodrecipesapp.domain.model

data class Review(
    val userId: Int,
    val userName: String,
    val userLastname: String,
    val rating: Int,
    val comment: String
)
