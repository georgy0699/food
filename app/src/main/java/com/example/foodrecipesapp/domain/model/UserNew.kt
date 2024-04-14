package com.example.foodrecipesapp.domain.model

data class UserNew (
    val id: Int,
    val firstName: String,
    val email: String,
    val lastName: String,
    val phoneNumber: String,
    val password: String,
)