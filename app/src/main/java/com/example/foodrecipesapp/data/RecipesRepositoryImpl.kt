package com.example.foodrecipesapp.data

import com.example.foodrecipesapp.data.db.RecipesDatabase
import com.example.foodrecipesapp.data.db.UserData
import com.example.foodrecipesapp.data.db.model.FavouriteSW
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Product
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.Review
import com.example.foodrecipesapp.domain.model.Step

class RecipesRepositoryImpl(private val db: RecipesDatabase, private val userData: UserData): IRecipesRepository {
    override suspend fun getAllRecipes(): Result<List<Recipe>> {
        return try {
            val recipesSW = db.recipesDao().getAllRecipes()
            val recipes = recipesSW.map { recipeSW ->
                val rating = db.recipesDao().calculateRatingForRecipe(recipeSW.id)
                val category = db.recipesDao().getCategoryById(recipeSW.categoryId).name
                val isFav = db.recipesDao().isRecipeFavourite(recipeSW.id)
                recipeSW.toDomain(rating,category,isFav)
            }
            Result.Success(recipes)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getRecipeById(recipeId: Int): Result<Recipe> {
        return try {
            val recipeSW = db.recipesDao().getRecipeById(recipeId)
            val rating = db.recipesDao().calculateRatingForRecipe(recipeSW.id)
            val category = db.recipesDao().getCategoryById(recipeSW.categoryId).name
            val isFav = db.recipesDao().isRecipeFavourite(recipeSW.id)
            Result.Success(recipeSW.toDomain(rating,category,isFav))
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getRecipeProducts(recipeId: Int): Result<List<Product>> {
        return try {
            val recipeProducts = db.recipesDao().getRecipeProducts(recipeId)
            val result = mutableListOf<Product>()
            recipeProducts.forEach {
                val product = db.recipesDao().getProductById(it.productId)
                result.add(product.toDomain(it.gram))
            }
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getRecipeSteps(recipeId: Int): Result<List<Step>> {
        return try {
            val recipeSteps = db.recipesDao().getStepsForRecipe(recipeId)
            Result.Success(recipeSteps.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val products = db.recipesDao().getAllProducts()
            Result.Success(products.map { it.toDomain(0) })
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val categories = db.recipesDao().getAllCategories()
            Result.Success(categories.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }


    override suspend fun getRecipeReviews(recipeId: Int): Result<List<Review>> {
        return try {
            val recipeReviews = db.recipesDao().getReviewsForRecipe(recipeId)
            val result = recipeReviews.map {
                val user = db.userDao().getUserInfoById(it.userId)
                it.toDomain(user!!.firstName, user.lastName)
            }
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun getUserFavouriteRecipes(): Result<List<Recipe>> {
        return try {
            val recipesSW = db.recipesDao().getUserFavoriteRecipes(userData.getUserId().toInt())
            val recipes = recipesSW.map { recipeSW ->
                val rating = db.recipesDao().calculateRatingForRecipe(recipeSW.id)
                val category = db.recipesDao().getCategoryById(recipeSW.categoryId).name
                val isFav = db.recipesDao().isRecipeFavourite(recipeSW.id)
                recipeSW.toDomain(rating,category,isFav)
            }
            Result.Success(recipes)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    override suspend fun uploadRecipe(recipe: Recipe, recipeSteps: List<Step>, recipeProducts: List<Product>): Result<String> {
        return try {
            val categoryId = db.recipesDao().getCategoryByName(recipe.category).id
            val rcp = db.recipesDao().insertRecipe(recipe.toSW(userData.getUserId().toInt(),categoryId)).toInt()
            db.recipesDao().insertRecipeSteps(recipeSteps.map { it.toSW(rcp) })
            db.recipesDao().insertRecipeProducts(recipeProducts.map { it.toSW(rcp) })
            Result.Success("Комментарий опубликован")
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }

    override suspend fun addReview(review: Review, recipeId: Int): Result<String> {
        return try {
            db.recipesDao().insertReview(review.toSW(recipeId, userData.getUserId().toInt()))
            Result.Success("Комментарий опубликован")
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }

    override suspend fun addToFavourite(recipeId: Int): Result<String> {
        return try {
            val fav = FavouriteSW(
                id = 0,
                recipeId = recipeId,
                userId = userData.getUserId().toInt()
            )
            db.recipesDao().addToFavorites(fav)
            Result.Success("Добавлено в избранное")
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }

    override suspend fun removeFromFavourite(recipeId: Int): Result<String> {
        return try {
            db.recipesDao().removeFromFavorites(recipeId, userData.getUserId().toInt())
            Result.Success("Удалено из избранного")
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }
}