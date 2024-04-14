package com.example.foodrecipesapp.domain.model

data class Product(
    val id: Int,
    val name: String,
    val imgUrl: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbohydrates: Int,
    val gram: Int,
)
