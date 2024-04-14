package com.example.foodrecipesapp.domain

import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Product
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.Review
import com.example.foodrecipesapp.domain.model.Step

interface IRecipesRepository {
    suspend fun getAllRecipes(): Result<List<Recipe>>
    suspend fun getRecipeById(recipeId: Int): Result<Recipe>
    suspend fun getUserFavouriteRecipes(): Result<List<Recipe>>
    suspend fun getRecipeProducts(recipeId: Int): Result<List<Product>>
    suspend fun getRecipeSteps(recipeId: Int): Result<List<Step>>
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getAllCategories(): Result<List<Category>>
    suspend fun getRecipeReviews(recipeId: Int): Result<List<Review>>
    suspend fun uploadRecipe(recipe: Recipe, recipeSteps: List<Step>, recipeProducts: List<Product>): Result<String>
    suspend fun addReview(review: Review, recipeId: Int): Result<String>
    suspend fun addToFavourite(recipeId: Int): Result<String>
    suspend fun removeFromFavourite(recipeId: Int): Result<String>
}