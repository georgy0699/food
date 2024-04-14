package com.example.foodrecipesapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodrecipesapp.data.db.model.CategorySW
import com.example.foodrecipesapp.data.db.model.FavouriteSW
import com.example.foodrecipesapp.data.db.model.ProductSW
import com.example.foodrecipesapp.data.db.model.RecipeProductsSW
import com.example.foodrecipesapp.data.db.model.RecipeSW
import com.example.foodrecipesapp.data.db.model.RecipeStepsSW
import com.example.foodrecipesapp.data.db.model.ReviewSW

@Dao
interface RecipesDao {
    // Получить все рецепты
    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeSW>

    // Получить рецепт по id
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeSW

    // Получить рецепты по указанной категории
    @Query("SELECT * FROM recipes WHERE category_id = :categoryId")
    suspend fun getRecipesByCategory(categoryId: Int): List<RecipeSW>

    // Получить рецепты находящиеся у пользователя в избранном
    @Query("SELECT * FROM recipes WHERE id IN (SELECT recipe_id FROM favorites WHERE user_id = :userId)")
    suspend fun getUserFavoriteRecipes(userId: Int): List<RecipeSW>

    // Получить шаги для рецепта
    @Query("SELECT * FROM recipe_steps WHERE recipe_id = :recipeId ORDER BY step_number")
    suspend fun getStepsForRecipe(recipeId: Int): List<RecipeStepsSW>

    // Получить продукты для рецепта
    @Query("SELECT * FROM products WHERE id IN (SELECT product_id FROM recipe_products WHERE recipe_id = :recipeId)")
    suspend fun getProductsForRecipe(recipeId: Int): List<ProductSW>

    @Query("SELECT * FROM recipe_products WHERE recipe_id = :recipeId")
    suspend fun getRecipeProducts(recipeId: Int): List<RecipeProductsSW>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductSW

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductSW>

    // Получить рецензии
    @Query("SELECT * FROM reviews WHERE recipe_id = :recipeId")
    suspend fun getReviewsForRecipe(recipeId: Int): List<ReviewSW>

    // Рассчитать рейтинг
    @Query("SELECT AVG(rating) FROM reviews WHERE recipe_id = :recipeId")
    suspend fun calculateRatingForRecipe(recipeId: Int): Double

    @Query("SELECT COUNT(*) > 0 FROM favorites WHERE recipe_id = :recipeId")
    suspend fun isRecipeFavourite(recipeId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorites(favorite: FavouriteSW)

    @Query("DELETE FROM favorites WHERE recipe_id = :recipeId AND user_id = :userId")
    suspend fun removeFromFavorites(recipeId: Int, userId: Int)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): CategorySW

    @Query("SELECT * FROM categories WHERE  name = :categoryName")
    suspend fun getCategoryByName(categoryName: String): CategorySW

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategorySW>

    // Добавить рецепт
    @Insert
    suspend fun insertRecipe(recipe: RecipeSW): Long

    @Insert
    suspend fun insertRecipes(recipes: List<RecipeSW>)

    @Insert
    suspend fun insertCategories(categories: List<CategorySW>)

    @Insert
    suspend fun insertProducts(products: List<ProductSW>)

    // Добавить шаги
    @Insert
    suspend fun insertRecipeSteps(steps: List<RecipeStepsSW>)

    // Добавить продукты
    @Insert
    suspend fun insertRecipeProducts(products: List<RecipeProductsSW>)

    // Добавить рецензию на рецепт
    @Insert
    suspend fun insertReview(review: ReviewSW)
}