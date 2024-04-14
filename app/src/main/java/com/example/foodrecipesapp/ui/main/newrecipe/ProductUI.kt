package com.example.foodrecipesapp.ui.main.newrecipe

data class ProductUI(
    val id: Int,
    val name: String,
    val imgUrl: String,
    var gram: Int,
    var isChecked: Boolean
)